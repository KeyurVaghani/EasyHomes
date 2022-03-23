import React, { lazy, Suspense, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Routes, Route } from 'react-router-dom';

import Container from '@mui/material/Container';

import './App.css';
import { updateUserLoggedInStatus } from './reducers/app/appSlice';
import Login from './components/login/Login';
import NavBar from './components/nav-bar/Navbar';
import HomeTabs from './components/home-tabs/HomeTabs';
import FabMenu from './components/fab-menu/FabMenu';
// const NavBar = lazy(() => import('./components/nav-bar/Navbar'));

import VideoCall from './components/video-call/VideoCallWebRTC';
import ChatRoom from './components/chat-room/ChatRoom';

function App() {
  const dispatch = useDispatch();
  const isUserLoggedIn = useSelector(state => state.app.isUserLoggedIn);


  setTimeout(() => { // TODO setTimeout is used just to mock auto login behaviour
    dispatch(updateUserLoggedInStatus({ isUserLoggedIn: true }))
  }, 3000);


  return (
    <>
    {/* TODO add protected route and lazy load'Home' component after login functionality is completed */}
      <Suspense fallback={<small>Loading...</small>}>
        <Routes>
            <Route path="/" element={false ? 
              <>
                <NavBar />
                <Container>
                  <HomeTabs />
                  <FabMenu />
                </Container>
              </> :
              <ChatRoom />} />
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
