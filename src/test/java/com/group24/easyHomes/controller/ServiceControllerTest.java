package com.group24.easyHomes.controller;

import com.group24.easyHomes.model.Services;
import com.group24.easyHomes.service.AppUserService;
import com.group24.easyHomes.service.ServicesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @MockBean
    private ServicesService mockService;

    @MockBean
    private AppUserService userService;

    final static String service =" {\n" +
            "        \"service_id\": 16,\n" +
            "        \"service_name\": \"Halifax Tiffin Services\",\n" +
            "        \"service_type\": \"Food Delivery\",\n" +
            "        \"cost\": 199,\n" +
            "        \"plan\": \"monthly\",\n" +
            "        \"description\": \"Welcome to Canada's food guide. ... Eat a variety of healthy foods each day. Healthy foods. Healthy eating is more than the foods\",\n" +
            "        \"city\": \"halifax\",\n" +
            "        \"province\": \"NS\",\n" +
            "        \"country\": \"Canada\",\n" +
            "        \"pincode\": \"h3h5k3\",\n" +
            "        \"address\": \"2040, street\",\n" +
            "        \"user_id\": 1,\n" +
            "        \"user_name\": \"Daman Kaur\",\n" +
            "        \"posted_on\": null\n" +
            "    },";

    Services serviceResponse = new Services("Halifax Tiffin Services",
            "Food Delivery",
            199,
            "monthly",
            "Welcome to Canada's food guide. ... Eat a variety of healthy foods each day. Healthy foods. Healthy eating is more than the foods",
            "halifax",
            "NS",
            "Canada",
            "h3h5k3",
            "2040, street",
            4L);

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getServices_Success() throws Exception {

        Services services = new Services();

        List<Services> allServices = new ArrayList<>();
        allServices.add(services);

        given(mockService.listAll()).willReturn(allServices);

        mockMvc.perform(get("/service/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeService_SUCCESS() throws Exception {
        when(mockService.delete(Constants.serviceID)).thenReturn("SUCCESS");
        mockMvc.perform(delete("/service/services/{serviceId}", Constants.serviceID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeService_ERROR() throws Exception {
        when(mockService.delete(Constants.serviceID)).thenReturn("ERROR");
        MockHttpServletRequestBuilder request =delete("/service/services/{serviceId}", Constants.serviceID);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateProperty_ERROR() throws Exception {
        when(mockService.updateService(Constants.serviceID, serviceResponse)).thenReturn(null);
        MockHttpServletRequestBuilder request  = put("/service/services/{serviceId}/update", Constants.serviceID);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateService_SUCCESS() throws Exception {
        when(mockService.updateService(Constants.serviceID,serviceResponse)).thenReturn(serviceResponse);
        MockHttpServletRequestBuilder request = put("/service/services/{serviceId}/update", Constants.serviceID);
        request= request.contentType(MediaType.APPLICATION_JSON).content(service.getBytes());
        mockMvc.perform(request).andExpect(status().isNoContent());
    }

}
