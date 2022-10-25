import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import loginReducer from "../_slice/loginSlice";

const store = configureStore({
  reducer: {
    login: loginReducer,
  },
});

export default store;
