import React, {useState} from "react";
import axios from "axios";
import {FAVORITE_PROPERTY, FAVORITE_SERVICE, GET_PROPERTY, GET_SERVICE} from "../../constants/Api";
import {
    Avatar, Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    CardMedia, Chip,
    Grid,
    IconButton,
    Rating,
    Stack
} from "@mui/material";

import {useDispatch} from "react-redux";
import Service from "../service/Service";
import {AddFavorite} from "../Icons";
import Typography from "@mui/material/Typography";
import {openModel, openModelProperty} from "../../reducers/app/appSlice";
import Property from "../property/Property";

const style = {
    ServiceFeed: {
        backgroundColor: "#F5F5F5",
        borderRadius: 25,
    },
};

const RenderService = ({service}, index) => {
    const dispatch = useDispatch();
    const blobData = service.images[0]?.image_data;

    const [favS, setFavS] = useState(false);
    const [btnColor, setBtnColor] = useState("grey");
    const [favSId, setFavSId] = useState(0);

    const InitFavState = () => {
        axios.get(FAVORITE_SERVICE + localStorage.getItem("userId"))
            .then((response) => {
                // console.log(response.data);
                let favSArr = response.data;
                favSArr.map((item) => {
                    if (item.service_id === service.service_id) {
                        setBtnColor("red");
                        setFavSId(item.favorite_service_id);
                        setFavS(true);
                    }
                })
            })
    }

    const ToggleFavS = (favSId) => {
        setFavS((favS) => {
            if (favS === true && favSId !== 0) {
                let div = document.getElementById(service.service_id);
                let divParent = div.parentElement;
                if (divParent !== null) {
                    divParent.removeChild(div);
                }
                axios.delete(FAVORITE_SERVICE + "delete/" + favSId)
                    .then((response) => {
                        setBtnColor("grey");
                        setFavS(false);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
            if (favS === false) {
                axios.post(FAVORITE_SERVICE + "add", {
                    user_id: localStorage.getItem("userId"),
                    service_id: service.service_id
                })
                    .then((response) => {
                        console.log(response.data);
                        setBtnColor("red");
                        setFavS(true);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        });
    }

    InitFavState();

    return (
        <>
            <Service/>
            <div style={{marginTop: '5%'}} id={service.service_id}>
                <Card>
                    <CardHeader
                        avatar={
                            <Avatar sx={{bgcolor: "red"}} aria-label="recipe">
                                R
                            </Avatar>
                        }
                        title="John Dona"
                        subheader="7th March 2022"
                    />
                    <CardMedia
                        component="img"
                        height="194"
                        image={`data:image/jpeg;base64,${blobData}`}
                    />
                    <CardActions disableSpacing style={{justifyContent: 'space-between'}}>
                        <IconButton aria-label="add to favorites"
                                    style={{color: btnColor}}
                                    onClick={() => {
                                        ToggleFavS(favSId);
                                    }}>
                            <AddFavorite/>
                        </IconButton>
                        <Rating
                            name="simple-controlled"
                            value={1}
                        />
                    </CardActions>
                    <CardContent style={{paddingTop: '1%'}}>
                        <div style={{justifyContent: 'space-between', display: 'flex', flexDirection: 'row'}}>
                            <Typography fontSize={24} fontWeight='bold'>{service.service_name}</Typography>
                            <Typography fontSize={24} fontWeight='bold'>${service.cost}</Typography>
                        </div>
                        <Typography>{service.description > 100 ?
                            service.description.substring(0, 100) + '...'
                            : service.description}</Typography>

                        <Typography fontSize={16} fontWeight='bold'>Subscription</Typography>
                        <Stack direction="row" spacing={1}><Chip size="small" label="Weekly"/>
                            <Chip size="small" label="Monthly"/>
                            <Chip size="small" label="Yearly"/></Stack>

                        <Typography marginTop={2.5}
                                    fontSize={16}>{service.address + " ," + service.pincode}</Typography>

                        <Typography fontSize={16}>{service.city + ",  "
                        + service.province + ", " + service.country} </Typography>
                    </CardContent>
                    <CardActions>
                        <Button onClick={() => dispatch(openModel({
                            homeDialog: {
                                isOpen: true,
                                service: service
                            }
                        }))}
                                size="small">Learn More</Button>
                    </CardActions>
                </Card>
            </div>
        </>
    );
};

const RenderProperty = ({property}, index) => {
    const dispatch = useDispatch();
    const blobData = property.images[0]?.image_data;

    const [favP, setFavP] = useState(false);
    const [btnColor, setBtnColor] = useState("grey");
    const [favPId, setFavPId] = useState(0);

    const InitFavState = () => {
        axios.get(FAVORITE_PROPERTY + localStorage.getItem("userId"))
            .then((response) => {
                // console.log(response.data);
                let favPArr = response.data;
                favPArr.map((item) => {
                    if (item.property_id === property.property_id) {
                        // console.log("initColor"+btnColor);
                        setBtnColor("red");
                        setFavPId(item.favorite_property_id);
                        setFavP(true);
                    }
                })
            })
    }

    const ToggleFavP = (favPId) => {
        setFavP((favP) => {
            if (favP === true && favPId !== 0) {
                let div = document.getElementById(property.property_id);
                let divParent = div.parentElement;
                if (divParent !== null) {
                    divParent.removeChild(div);
                }
                axios.delete(FAVORITE_PROPERTY + "delete/" + favPId)
                    .then((response) => {
                        // console.log(response.data);
                        setBtnColor("grey");
                        setFavP(false);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
            if (favP === false) {
                axios.post(FAVORITE_PROPERTY + "add", {
                    user_id: localStorage.getItem("userId"),
                    property_id: property.property_id
                })
                    .then((response) => {
                        // console.log(response.data);
                        setBtnColor("red");
                        setFavP(true);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        });
    }

    InitFavState();

    return (
        <>
            <Property/>
            <div style={{marginTop: '5%'}} id={property.property_id}>
                <Card>
                    <CardHeader
                        avatar={
                            <Avatar sx={{bgcolor: "red"}} aria-label="recipe">
                                R
                            </Avatar>
                        }
                        title="John Dona"
                        subheader="7th March 2022"/>
                    <CardMedia
                        component="img"
                        height="194"
                        image={`data:image/jpeg;base64,${blobData}`}
                    />
                    <CardActions disableSpacing style={{justifyContent: 'space-between'}}>
                        <IconButton aria-label="add to favorites"
                                    style={{color: btnColor}}
                                    onClick={() => {
                                        ToggleFavP(favPId);
                                    }}>
                            <AddFavorite/>
                        </IconButton>
                        <Rating
                            name="simple-controlled"
                            value={1}
                        />
                    </CardActions>
                    <CardContent style={{paddingTop: '1%'}}>
                        <div style={{justifyContent: 'space-between', display: 'flex', flexDirection: 'row'}}>
                            <Typography fontSize={24} fontWeight='bold'>{property.property_name}</Typography>
                            <Typography fontSize={24} fontWeight='bold'>${property.rent}</Typography>
                        </div>
                        <Typography>{"Amenities: " + property.amenities}</Typography>
                        <Typography>{"Bathrooms: " + property.bathrooms}</Typography>
                        <Typography>{"Bedrooms: " + property.bedrooms}</Typography>
                        <Typography>Parking: {property.parking_included ? "Available" : "Not available"}</Typography>

                        <Typography marginTop={2.5}
                                    fontSize={16}>{property.address.location + " ," + property.address.postal_code}</Typography>

                        <Typography fontSize={16}>{property.address.city + ",  "
                        + property.address.province + ", " + property.address.country} </Typography>
                    </CardContent>
                    <CardActions>
                        <Button onClick={() => dispatch(openModelProperty({
                            homeDialogProperty: {
                                isOpen: true,
                                property: property
                            }
                        }))}
                                size="small">Learn More</Button>
                    </CardActions>
                </Card>
            </div>
        </>
    );
};

function FavList() {
    const userId = localStorage.getItem("userId");

    const [favPData, setFavPData] = useState([]);
    const [properties, setProperties] = useState([]);
    let favP = [];

    const [favSData, setFavSData] = useState([]);
    const [services, setServices] = useState([]);
    let favS = []

    React.useEffect(() => {
        axios.get(FAVORITE_PROPERTY + userId).then((response) => {
            setFavPData(response.data);
        });
    }, []);

    React.useEffect(() => {
        axios.get(GET_PROPERTY).then((response) => {
            setProperties(response.data);
        });
    }, [])

    React.useEffect(() => {
        axios.get(FAVORITE_SERVICE + userId).then((response) => {
            setFavSData(response.data);
        });
    }, []);

    React.useEffect(() => {
        axios.get(GET_SERVICE).then((response) => {
            setServices(response.data);
        });
    }, [])

    function findFavP(favPData, properties) {
        if (favPData != null && properties != null) {
            favPData.map(fpd => {
                properties.map(p => {
                    if (fpd.property_id === p.property_id) {
                        favP.push(p);
                    }
                })
            })
        }
    }

    function findFavS(favSData, services) {
        if (favSData != null && services != null) {
            favSData.map(fsd => {
                services.map(s => {
                    if (fsd.service_id === s.service_id) {
                        favS.push(s);
                    }
                })
            })
        }
    }

    findFavP(favPData, properties);
    findFavS(favSData, services);

    return (
        <div>
            <div className="fav-page">
                <div className="fav-body">
                    <main className="favorites">
                        <div className="fav-properties">
                            <h2 className="fav-p-title">My Favorite Properties</h2>
                            {favP.map(item => (
                                <div>
                                    <RenderProperty property={item}/>
                                </div>
                            ))}
                        </div>
                        <hr style={{marginTop: '5%'}}/>
                        <div className="fav-services">
                            <h2 className="fav-s-title">My Favorite Services</h2>
                            {favS.map(item => (
                                <div>
                                    <RenderService service={item}/>
                                </div>
                            ))}
                        </div>
                    </main>
                </div>
            </div>
        </div>
    );
}

export default FavList;