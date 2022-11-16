import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import windowReducer from "../_slice/windowSlice";
import loginReducer from "../_slice/loginSlice";

import websocketReducer from "../_slice/websocketSlice";
import chattingReducer from "../_slice/chattingSlice";
import memberInfoReducer from "../_slice/memberInfoSlice";
import tempGameListReducer from "../_slice/tempGameSlice"
import gameReducer from "../_slice/gameSlice";
import roomReducer from "../_slice/roomSlice";
import rankReducer from "../_slice/rankSlice";

const store = configureStore({
  reducer: {
    windowData: windowReducer,
    login: loginReducer,
    websocket: websocketReducer,
    chatting: chattingReducer,
    room: roomReducer,
    memberInfo: memberInfoReducer,
    game: gameReducer,
    tempGame: tempGameListReducer,
    rank: rankReducer,
  },
  middleware: getDefaultMiddleware({
    serializableCheck: false,
  }),
});

export default store;
