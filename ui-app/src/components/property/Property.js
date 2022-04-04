<<<<<<< HEAD
import "bootstrap";
import { Button, Carousel } from "react-bootstrap";
import React, { useState } from "react";
import { Dialog, Rating } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { openModelProperty } from "../../reducers/app/appSlice";
import axios from "axios";
import { PROPERTY_SCHEDULE_MEETING } from "../../constants/Api";
import Popup from "../Popup";
import {Container, Grid, TextField } from "@mui/material";
import DateTimePicker from "react-datetime-picker";

const Property = () => {
  const dispatch = useDispatch();

  const isOpen = useSelector((state) => state.app.homeDialogProperty.isOpen);
  const property = useSelector(
    (state) => state.app.homeDialogProperty.property
  );

  console.log(property);

  const [popIsOpen, setpopIsOpen] = useState(false);
  const [messageS, setMessageS] = useState("");
  const [dateandtimee, setDateandtimee] = useState(new Date());

  const togglePopupOpen = (e) => {
    setpopIsOpen(true);
  };

  const togglePopupClose = () => {
    setpopIsOpen(false);
  };

  const scheduleMeeting = (property, event) => {
    if (event) {
      event.preventDefault();
    }

    const scheduleMeetingDetails = {
      user_id: localStorage.getItem("userId"),
      property_id: property?.property_id,
      property_user_id: property?.user_id,
      message: messageS,
      appointmentTime: dateandtimee,
    };

    setpopIsOpen(false);

    console.log("Service details:  " + JSON.stringify(scheduleMeetingDetails));

    axios
      .post(PROPERTY_SCHEDULE_MEETING, JSON.stringify(scheduleMeetingDetails), {
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        console.log(response.data);
        alert("Email sent.");
      })
      .catch(function (error) {
        if (error.response) {
          alert(error.response.data);
        }
      });
  };

  return (
    <Dialog
      fullWidth
      maxWidth="md"
      open={isOpen}
      onClose={() =>
        dispatch(
          openModelProperty({
            homeDialogProperty: { isOpen: false, property: null },
          })
        )
      }
    >
      <div
        style={{
          padding: "5%",
          alignSelf: "center",
        }}
      >
        <div style={{ alignSelf: "center", width: "100%" }}>
          <Carousel style={{ border: 5, borderColor: "red" }} slide={false}>
            {property?.images.map((image, index) => {
              const blobData = image.image_data;
              return (
                <Carousel.Item>
                  <img
                    className="d-block w-100"
                    style={{
                      borderRadius: "5%",
                      maxWidth: "100%",
                      maxHeight: "400px",
                      width: "100%",
                      height: "500px",
                    }}
                    src={`data:image/jpeg;base64,${blobData}`}
                    alt="First slide"
                  />
                </Carousel.Item>
              );
            })}
          </Carousel>
        </div>
        <div
          style={{
            marginTop: "10%",
          }}
        >
          <h2 style={{ fontWeight: "bold" }}>{property?.service_name}</h2>
          <h4> {"$" + property?.rent}</h4>
          <Rating name="simple-controlled" value={1} />
        </div>
        <div
            style={{
              display: "flex",
              width: "100%",
              justifyContent: "space-between",
            }}
          >
            <Button
              onClick={(e) => {
                togglePopupOpen(e);
              }}
            >
              Schedule a Meeting
            </Button>
            {popIsOpen && (
              <Popup
                content={
                  <>
                    <Container maxWidth="md">
                      <Grid container spacing={2}>
                        <Grid item xs={6}>
                          <TextField
                            required
                            id="message"
                            name="messgae"
                            label="Enter a message"
                            value={messageS}
                            onChange={(e) => {
                              setMessageS(e.target.value);
                            }}
                            fullWidth
                          />
                        </Grid>
                        <Grid item xs={6}>
                          <DateTimePicker
                            onChange={setDateandtimee}
                            value={dateandtimee}
                          />
                        </Grid>
                      </Grid>
                      <div>
                        <Button
                          style={{
                            margin: "10px 10px 0px 10px",
                          }}
                          item
                          xs={6}
                          onClick={(e) => {
                            scheduleMeeting(property, e);
                          }}
                          disabled={messageS.length === 0}
                        >
                          Submit
                        </Button>
                      </div>
                      <div>
                        <Button
                          style={{
                            margin: "10px 10px 0px 10px",
                          }}
                          item
                          xs={6}
                          onClick={(e) => {
                            togglePopupClose(e);
                          }}
                        >
                          Close
                        </Button>
                      </div>
                    </Container>
                  </>
                }
              />
            )}
          </div>

        <h3>Reviews</h3>
      </div>
    </Dialog>
  );
};
=======
import React from "react";
import Dialog from '@mui/material/Dialog';
import { Button, Typography } from "@mui/material";
import Carousel,{CarouselItem} from "../carosel/Carousel";

const Property = (props) => {
    const { open, setDialogOpenState,property } = props;

    const handleClose = () => {
       setDialogOpenState();
      };

      const handleDialogClose = () => {
        setDialogOpenState();
      }

  return (
    <Dialog fullWidth maxWidth='md' onBackdropClick={handleDialogClose} onClose={handleClose} open={open}>
    <div
    style={{
      padding: "5%",
        alignSelf:'center'
    }}>
        <div style={{alignSelf:'center',width:'100%'}}>
       <Carousel>
      {property?.images.map((item)=>{
            const blobData = item.image_data
            const imageSrc = blobData ? `data:image/jpeg;base64,${blobData}` : ''
          return(
            <CarouselItem>
            <img
            style={{maxWidth:'100%',maxHeight:'700px',width:'100%',height:'700px',}}
            src={imageSrc}
          /></CarouselItem>)
          })
        }
        </Carousel>
        </div>
          <Typography fontSize={34} style={{ fontWeight: "bold" }}>{property?.property_name}</Typography>
          <Typography fontSize={28}> {'$'+property?.rent}</Typography>
          <Button style={{marginTop:'1.5%',marginBottom:'1.5%'}} variant="contained">
            Schedule Meeting
          </Button>
          <Typography fontSize={28}>Reviews</Typography>
        </div>
    </Dialog>);
 }
>>>>>>> 3da820e2aa782ccecefc28bd8dc99e8d2d392e6a

export default Property;
