import React, { useState } from "react";

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from 'axios';

function Copyright(props) {
  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        EasyHomes
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const theme = createTheme();

const Register = (props) => {

  const initialState = {
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  };

  const [user, setUser] = useState(initialState);

  const userChange = (event) => {
    const { name, value } = event.target;
    setUser({ ...user, [name]: value });
  };


  const saveUser = () => {

      axios
      .post('http://localhost:8080/user/register',  {
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        password: user.password,
      },
      {
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
        }
      })
  };

        return (
             <ThemeProvider theme={theme}>
                 <Container component="main" maxWidth="xs">
                   <CssBaseline />
                   <Box
                     sx={{
                       marginTop: 8,
                       display: 'flex',
                       flexDirection: 'column',
                       alignItems: 'center',
                     }}
                   >
                     <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                       <LockOutlinedIcon />
                     </Avatar>
                     <Typography component="h1" variant="h5">
                       Sign up
                     </Typography>
                     <Box component="form" noValidate onChange={userChange} sx={{ mt: 3 }}>
                       <Grid container spacing={2}>
                         <Grid item xs={12} sm={6}>
                           <TextField
                             autoComplete="given-name"
                             name="firstName"
                             value={user.firstName}
                             onChange={userChange}
                             required
                             fullWidth
                             id="firstName"
                             label="First Name"
                             autoFocus
                           />
                         </Grid>
                         <Grid item xs={12} sm={6}>
                           <TextField
                             required
                             fullWidth
                             id="lastName"
                             label="Last Name"
                             name="lastName"
                             value={user.lastName}
                             onChange={userChange}
                             autoComplete="family-name"
                           />
                         </Grid>
                         <Grid item xs={12}>
                           <TextField
                             required
                             fullWidth
                             id="email"
                             label="Email Address"
                             name="email"
                             value={user.email}
                             onChange={userChange}
                             autoComplete="email"
                           />
                         </Grid>
                         <Grid item xs={12}>
                           <TextField
                             required
                             fullWidth
                             name="password"
                             label="Password"
                             type="password"
                             id="password"
                             value={user.password}
                             onChange={userChange}
                             autoComplete="new-password"
                           />
                         </Grid>

                       </Grid>
                       <Button
                         type="submit"
                         fullWidth
                         variant="success"
                         onClick={saveUser}
                         sx={{ mt: 3, mb: 2 }}
                       >
                         Sign Up
                       </Button>
                       <Grid container justifyContent="flex-end">
                         <Grid item>
                           <Link href="login" variant="body2">
                             Already have an account? Sign in
                           </Link>
                         </Grid>
                       </Grid>
                     </Box>
                   </Box>
                   <Copyright sx={{ mt: 5 }} />
                 </Container>
               </ThemeProvider>

        );
    }


export default Register;