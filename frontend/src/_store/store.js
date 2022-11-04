import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import windowReducer from "../_slice/windowSlice";
import loginReducer from "../_slice/loginSlice";

import websocketReducer from "../_slice/websocketSlice";
import memberInfoReducer from "../_slice/memberInfoSlice";
import tempGameListReducer from "../_slice/tempGameSlice"
import gameReducer from "../_slice/gameSlice";
import roomReducer from "../_slice/roomSlice";

const store = configureStore({
  reducer: {
    windowData: windowReducer,
    login: loginReducer,
    websocket: websocketReducer,
    room: roomReducer,
    memberInfo: memberInfoReducer,
    game: gameReducer,
    tempGame: tempGameListReducer,
  },
});

export default store;
