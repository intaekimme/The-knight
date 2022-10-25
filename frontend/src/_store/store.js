import { configureStore, getDefaultMiddleware } from '@reduxjs/toolkit';
import loginReducer from '../_slice/LoginSlice';

const store = configureStore({
  reducer: {
    login: loginReducer,
  }
});

export default store;