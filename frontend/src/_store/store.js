import { configureStore, getDefaultMiddleware } from '@reduxjs/toolkit';
import {tempValueAction} from '../_slice/tempSlice';

const store = configureStore({
  reducer: {
    temp: tempValueAction,
  }
});

export default store;