
package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.PropertyAddress;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.repository.PropertyRepository;
import com.group24.easyHomes.service.AppUserService;
import com.group24.easyHomes.service.PropertyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @MockBean
    private PropertyService service;

    @MockBean
    private AppUserService userService;

    final static String property = "{" +  "\"user_id\": 1,\n"+
            "\"property_name\": \"Apt 605 killam Apartments\",\n" +
            "        \"address\":{\n" +
            "            \"location\" : \"University Street\",\n" +
            "            \"city\": \"Halifax\",\n" +
            "            \"province\":\"NS\",\n" +
            "            \"country\": \"Canada\",\n" +
            "            \"postal_code\": \"H2Y8IK\"\n" +
            "        },\n" +
            "        \"amenities\":\"Laundry\",\n" +
            "        \"property_type\":\"1 BHK\",\n" +
            "         \"bathrooms\":1,\n" +
            "         \"bedrooms\":1,\n" +
            "        \"parking_included\":\"true\",\n" +
            "        \"rent\":\"500.0\"\n" +
            "}";

    // create query string of PeopertyListQuery with property_name and all other properties
    static final String propertyListQuery = "{" +
            "\"property_name\":\"test\"," +
            "\"amenities\":\"Laundry\"," +
            "\"property_type\":\"House\"," +
            "\"numberOfBathrooms\":2," +
            "\"numberOfBedrooms\":2," +
            "\"parkingIncluded\":\"true\"," +
            "\"rent\":1000.0," +
            "\"city\":\"Halifax\"," +
            "\"province\":\"NS\"," +
            "\"country\":\"Canada\"," +
            "}";

    //PropertyListQuery filters = new PropertyListQuery("test", "Laundry", "House", 2, 2, true, 1000.0, "Halifax", "NS", "Canada");


    PropertyAddress address = new PropertyAddress
            ("University Street","Halifax","NS","Canada","H2Y8IK");;

    Property propertyResponse = new Property("Apt 605 Iris Apartments",
            address, "Laundry", "1 BHK", true,
            Constants.propertyRent, Constants.noOfBedrooms_1, Constants.noOfBedrooms_1,Constants.userId);

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getProperties() throws Exception {

        Property property1 = new Property();

        List<Property> allProperties = new ArrayList<>();
        allProperties.add(property1);

        given(service.listAll()).willReturn(allProperties);

        mockMvc.perform(get("/property/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getProperty() throws Exception {

        Property property1 = new Property();
        given(service.getProperty(Constants.propertyID)).willReturn(property1);
        mockMvc.perform(get("/property/{propertyID}", Constants.propertyID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void addProperty() throws Exception {

        doReturn(null).when(userService).getById(anyLong());
        doReturn(propertyResponse).when(service).addProperty(any(Property.class));
        MockHttpServletRequestBuilder request = post("/property/property");
        request= request.contentType(MediaType.APPLICATION_JSON).content(property.getBytes());
        mockMvc.perform(request).andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeProperty_SUCCESS() throws Exception {
        when(service.delete(Constants.propertyID)).thenReturn("SUCCESS");
        mockMvc.perform(delete("/property/properties/{propertyId}", Constants.propertyID))
                .andExpect(status().isNoContent());
    }



    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeProperty_ERROR() throws Exception {
        when(service.delete(Constants.propertyID)).thenReturn("ERROR");
        MockHttpServletRequestBuilder request =delete("/property/properties/{propertyId}", Constants.propertyID);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateProperty_ERROR() throws Exception {
        when(service.updateProperty(Constants.propertyID, propertyResponse)).thenReturn(null);
        MockHttpServletRequestBuilder request  = put("/property/properties/10/update", Constants.propertyID);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateProperty_SUCCESS() throws Exception {
        when(service.updateProperty(Constants.propertyID,propertyResponse)).thenReturn(propertyResponse);
        MockHttpServletRequestBuilder request = put("/property/properties/10/update", Constants.propertyID);
        request= request.contentType(MediaType.APPLICATION_JSON).content(property.getBytes());
        mockMvc.perform(request).andExpect(status().isNoContent());
    }


    // use mock mvc to test filter properties with request body
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withRequestBody() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setProperty_type("House");
        query.setNumberOfBathrooms(2);
        query.setNumberOfBedrooms(2);
        query.setParkingIncluded(true);
        query.setRent(1000.0);
        query.setCity("Halifax");
        query.setProvince("NS");
        query.setCountry("Canada");

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    // use mock mvc to test filter properties with request body with bad request
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_ERROR_withRequestBody() throws Exception {



        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setProperty_type("House");
        query.setNumberOfBathrooms(2);
        query.setNumberOfBedrooms(2);
        query.setParkingIncluded(true);
        query.setRent(1000.0);
        query.setCity("Halifax");
        query.setProvince("NS");
        query.setCountry("Canada");

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    // use mock mvc to test filter properties with request body with empty request body object and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withEmptyRequestBody() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();


        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    // use mock mvc to test filter properties with request body with all parameters as null and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withAllNullRequestBody() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
//        query.setProperty_name(null);
//        query.setProperty_type(null);
//        query.setNumberOfBathrooms(null);
//        query.setNumberOfBedrooms(null);
//        query.setParkingIncluded(null);
//        query.setRent(null);
//        query.setCity(null);
//        query.setProvince(null);
//        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // use mock mvc to test filter properties with request body with all parameters as empty string and null values then return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withAllEmptyRequestBody() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("");
        query.setProperty_type("");
        query.setAmenities("");
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity("");
        query.setProvince("");
        query.setCountry("");

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // use mock mvc to test filter properties with property_name as "test" and property_type as "House" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyNameAndPropertyType() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setProperty_type("House");
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

//        when(service.filterProperties(query)).thenReturn(properties);
//        String content = new ObjectMapper().writeValueAsString(query);
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // use mock mvc to test filter properties with property_name as "test" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyName() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_withPropertyName_noResults() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        List<Property> properties = new ArrayList<>();

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // use mock mvc to test filter properties with property_type as "House" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyType() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type("House");
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyType_noResults() throws Exception {
        List<Property> properties = new ArrayList<>();
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type("House");
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // use mock mvc to test filter properties with number of bedrooms as 2 and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBedrooms() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(2);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

//        when(service.filterProperties(query)).thenReturn(properties);
        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBedrooms_noResult() throws Exception {
        List<Property> properties = new ArrayList<>();

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(2);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // use mock mvc to test filter properties with number of bathrooms as 2 and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBathrooms() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(2);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBathrooms_noResults() throws Exception {
        List<Property> properties = new ArrayList<>();
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(2);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // use mock mvc to test filter properties with parking included as true and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withParkingIncluded() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(true);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

//        when(service.filterProperties(query)).thenReturn(properties);
        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_withPropertyWithParkingNotIncluded_noResult() throws Exception {
        List<Property> properties = new ArrayList<>();
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(true);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // use mock mvc to test filter properties with rent as 1000 and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_withPropertyWithRent_noResult() throws Exception {
        List<Property> properties = new ArrayList<>();
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(1000.0);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // use mock mvc to test filter properties with city as "Halifax" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCity() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity("Halifax");
        query.setProvince(null);
        query.setCountry(null);

//        when(service.filterProperties(query)).thenReturn(properties);
        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCity_noResult() throws Exception {
        List<Property> properties = new ArrayList<>();
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity("Halifax");
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // use mock mvc to test filter properties with province as "NS" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withProvince() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince("NS");
        query.setCountry(null);

//        when(service.filterProperties(query)).thenReturn(properties);
        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withProvince_noResult() throws Exception {
        List<Property> properties = new ArrayList<>();

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince("NS");
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // use mock mvc to test filter properties with country as "Canada" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCountry() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry("Canada");

//        when(service.filterProperties(query)).thenReturn(properties);
        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCountry_noResult() throws Exception {
        List<Property> properties = new ArrayList<>();

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry("Canada");

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // use mock mvc to test filter properties with property_name and amneties as "test" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyNameAndAmneties() throws Exception {

        List<Property> properties = new ArrayList<>();
        properties.add(Constants.property);

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setAmenities("Wifi");
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // use mock mvc to test filter properties with property_name and amneties as "test" and "Wifi" and return all properties
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyNameAndAmneties_noResult() throws Exception {

        List<Property> properties = new ArrayList<>();

        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("test");
        query.setAmenities("Wifi");
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(properties).when(service).filterProperties(any(PropertyListQuery.class));
        String content = new ObjectMapper().writeValueAsString(query);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/property/properties/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }





}

