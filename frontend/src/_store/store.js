import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import windowReducer from "../_slice/windowSlice";
import loginReducer from "../_slice/loginSlice";

import memberInfoReducer from "../_slice/memberInfoSlice";
import gameReducer from "../_slice/gameSlice";

const store = configureStore({
  reducer: {
    windowData: windowReducer,
    login: loginReducer,
    memberInfo: memberInfoReducer,
    game: gameReducer,
  },
});

export default store;
