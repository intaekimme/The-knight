import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import loginReducer from "../_slice/loginSlice";
import gameReducer from "../_slice/gameSlice";

const store = configureStore({
  reducer: {
    login: loginReducer,
    game: gameReducer,
  },
});

export default store;
