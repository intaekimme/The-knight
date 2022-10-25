import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import loginReducer from "../_slice/loginSlice";
import playersReducer from "../_slice/playersSlice";

const store = configureStore({
  reducer: {
    login: loginReducer,
    players: playersReducer,
  },
});

export default store;
