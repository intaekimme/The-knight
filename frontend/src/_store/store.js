import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import loginReducer from "../_slice/loginSlice";
import playersReducer from "../_slice/playersSlice";
import memberInfoReducer from "../_slice/memberInfoSlice";

const store = configureStore({
  reducer: {
    login: loginReducer,
    players: playersReducer,
    memberInfo: memberInfoReducer,
  },
});

export default store;
