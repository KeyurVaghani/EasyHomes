import "bootstrap";
import { Button, Carousel } from "react-bootstrap";
import React, { useState } from "react";
import { Dialog, Rating } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { openModel } from "../../reducers/app/appSlice";
import { Container } from "react-bootstrap";
import Payment from "../payment/payment";
import { SERVICE_BOOK_APPOINTMENT } from "../../constants/Api";
import Popup from "../Popup";
import axios from "axios";
import { Grid, TextField } from "@mui/material";
import DateTimePicker from "react-datetime-picker";

const Service = () => {
  const dispatch = useDispatch();
  const [launchPayPal, setLaunchPayPal] = React.useState(false);

  const isOpen = useSelector((state) => state.app.homeDialog.isOpen);
  const service = useSelector((state) => state.app.homeDialog.service);

  const [popIsOpen, setpopIsOpen] = useState(false);
  const [messageS, setMessageS] = useState("");
  const [dateandtimee, setDateandtimee] = useState(new Date());

  const togglePopupOpen = (e) => {
    setpopIsOpen(true);
  };

  const togglePopupClose = () => {
    setpopIsOpen(false);
  };

  const scheduleMeeting = (service, event) => {
    if (event) {
      event.preventDefault();
    }

    const scheduleMeetingDetails = {
      user_id: localStorage.getItem("userId"),
      service_id: service?.service_id,
      service_user_id: service?.user_id,
      message: messageS,
      appointmentTime: dateandtimee,
    };

    setpopIsOpen(false);

    console.log("Service details:  " + JSON.stringify(scheduleMeetingDetails));

    axios
      .post(SERVICE_BOOK_APPOINTMENT, JSON.stringify(scheduleMeetingDetails), {
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
        dispatch(openModel({ homeDialog: { isOpen: false, service: null } }))
      }
    >
      <div
        style={{
          padding: "5%",
        }}
      >
        <div style={{ alignSelf: "center", width: "100%" }}>
          <Carousel style={{ border: 5, borderColor: "red" }} slide={false}>
            {service?.images.map((image, index) => {
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
          <h2 style={{ fontWeight: "bold" }}>{service?.service_name}</h2>
          <h4> {"$" + service?.cost}</h4>
          <Rating name="simple-controlled" value={1} />
          <h5>{service?.description}</h5>
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
              Book an Appointment
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
                            scheduleMeeting(service, e);
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
            <Button
              onClick={() => {
                setLaunchPayPal(true);
              }}
            >
              Buy Service
            </Button>
          </div>
          <Container
            style={{
              marginTop: "1%",
              display: "flex",
              justifyContent: "center",
            }}
          >
            {launchPayPal ? <Payment service={service} /> : <div />}
          </Container>
          <h3>Reviews</h3>
        </div>
      </div>
    </Dialog>
  );
};

export default Service;
