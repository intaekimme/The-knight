import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import loginReducer from "../_slice/loginSlice";

import memberInfoReducer from "../_slice/memberInfoSlice";
import gameReducer from "../_slice/gameSlice";




const store = configureStore({
  reducer: {
    login: loginReducer,
    memberInfo: memberInfoReducer,
    game: gameReducer,
  },
});

export default store;
