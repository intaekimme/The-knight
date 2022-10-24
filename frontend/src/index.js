import React from 'react';
import { Provider } from 'react-redux';
import { createTheme, ThemeProvider } from '@mui/material';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';
import App from './App';
import ScrollToTop from "./ScrollToTop";
import Main from "./pages/Main";
import Information from "./pages/Information";
import Lobby from "./pages/Lobby";
import Rank from "./pages/Rank";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import UserPage from "./pages/UserPage";
import Game from "./pages/Game";
import store from './_store/store';

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
              <Route path="login" element={<Login />} />
              <Route path="signup" element={<Signup />} />
              <Route path="userpage" element={<UserPage />} />
              <Route path="game" element={<Game />} />
            </Route>
          </Routes>
        </ThemeProvider>
      </Provider>
  </BrowserRouter>
);