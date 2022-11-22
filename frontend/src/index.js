import React from 'react';
import { Provider, useSelector } from 'react-redux';
import { createTheme, ThemeProvider } from '@mui/material';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';
import App from './App';
import store from './_store/store';
import ScrollToTop from "./ScrollToTop";
import Main from "./pages/Main";
import Information from "./pages/Information";
import Lobby from "./pages/Lobby";
import Rank from "./pages/Rank";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import MemberPage from "./pages/MemberPage";
import Game from './pages/Game';
import ConnectWebsocket from './components/lobby/ConnectWebsocket';
import EnterRoom from './components/lobby/EnterRoom';
import Room from "./pages/Room";
import GoogleLogin from './commons/login/GoogleLogin';
import LoginToken from './commons/login/LoginToken';

const root = ReactDOM.createRoot(document.getElementById('root'));
const theme = createTheme({
  components: {
    // Name of the component
    MuiTypography: {
      defaultProps: {
        variantMapping: {
          body1: 'span',
          body2: 'span',
        },
      },
    },
  },
});

root.render(
  <BrowserRouter>
    <ScrollToTop />
      <Provider store={store}>
        <ThemeProvider theme={theme}>
          <Routes>
            <Route path="/" element={<App />}>
              <Route path="" element={<Main />} />
              <Route path="info" element={<Information />} />
              <Route path="lobby" element={<Lobby />} />
              <Route path="rank" element={<Rank />} />
              <Route path="game" element={<Game />} />
              <Route path="login" element={<Login />} />
              <Route path="islogin" element={<LoginToken />} />
              <Route path="signup" element={<Signup />} />
              <Route path="memberpage" element={<MemberPage />} />
              <Route path="connect-websocket/:gameId" element={<ConnectWebsocket/>}/>
              <Route path="/entryRoom/:gameId" element={<EnterRoom />} />
              <Route path="/room/:gameId" element={<Room />} />
            </Route>
          </Routes>
        </ThemeProvider>
      </Provider>
  </BrowserRouter>
);