<<<<<<< HEAD
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
=======
import React from "react";
import {Button, Rating, Dialog, Typography, Container } from "@mui/material";
import { useDispatch, useSelector } from 'react-redux';
import { openModel } from '../../reducers/app/appSlice';
import Payment from "../payment/payment";
import Carousel,{CarouselItem} from "../carosel/Carousel";
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import ServiceReviewForm from "./ServiceReviewForm";

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const Service = (props) => {

  const { open, setDialogOpenState,service } = props;
  const [launchPayPal, setLaunchPayPal ] = React.useState(false);
  const [succeeded, setSucceeded] = React.useState(false);
  const [toastContent, setToastContent] = React.useState('');
  const [openReview,setOpenReview] = React.useState(false);

  const dispatch = useDispatch();
  const handleSnackClose = () => {
    setSucceeded(false);
  }

  const handleDialogClose = () => {
    setLaunchPayPal(false);
    dispatch(openModel({ homeDialog:{isOpen:false,service:null} }))
  }

  const handleClose = () => {
    setDialogOpenState();
   };

  return (
    <Dialog fullWidth maxWidth='md' onBackdropClick={handleDialogClose} onClose={handleClose} open={open}>
    <div
    style={{
      padding: "5%",
    }}>
        <div style={{alignSelf:'center',width:'100%'}}>
          <Carousel>
            { service?.images.map((item , index)=>{
                const blobData = item.image_data
                const imageSrc = blobData ? `data:image/jpeg;base64,${blobData}` : ''
              return(
                <CarouselItem>
                <img style={{maxWidth:'100%',maxHeight:'700px',width:'100%',height:'700px',}}
                src={imageSrc}/>
                </CarouselItem>)
              })
            }
          </Carousel>
        </div>
        <Typography fontSize={34} style={{ fontWeight: "bold" }}>{service?.service_name}</Typography>
        <Typography fontSize={28}> {'$'+service?.cost}</Typography>
        <Typography>{service?.description}</Typography>
        <div style={
          {
            display:"flex",
            width:"100%",
            justifyContent: "space-between",
            marginTop:'1.5%',
            marginBottom:'1.5%  '
          }
        }>
          <Button variant="contained">
            Schedule Meeting
          </Button>
          <Button variant="contained" onClick ={() => {setLaunchPayPal(true)}} >
            Buy Service
          </Button>
        </div>
        {launchPayPal?<Payment service={service} setToastMessage={setSucceeded} />:<div/>}
        <Snackbar open={succeeded} autoHideDuration={6000} onClose={handleSnackClose}>
          <Alert onClose={handleSnackClose} severity="success" sx={{ width: '100%' }}>
          Payment Sucessful!
          </Alert>
        </Snackbar>
        <h3 style={{ fontWeight: "bold" }}>Reviews</h3>
        {service?.reviews.map((review, index) => {
            return (
              <div><Container style={
                {
                  marginTop:"2%",
                  marginLeft:-10
                  
                }
              }>
                <div style={{justifyContent:'space-between',display:"flex"}}>
                  <Typography fontSize={25} style={{ padding:0, margin:0 }}>{review.review_subject}</Typography>
                  <Rating readOnly name="simple-controlled" value={review.review_rating} />
                </div>
                <p style={{ padding:0, margin:0}}>{review.review_description}</p>
                <small style={{ padding:0, margin:0}}>Posted on:{review.posted_on}</small>
                <hr></hr>
                </Container>
              </div>
            );
          })}
      <ServiceReviewForm style = {{marginLeft:-30}} service = {service}/>
       </div>

    </Dialog>
    
  )
}
>>>>>>> 3da820e2aa782ccecefc28bd8dc99e8d2d392e6a

export default Service;
