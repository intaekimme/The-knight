import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { over, Client } from "stompjs";
import SockJS from "sockjs-client";
// import axios from 'axios';
import api from '../api/api';

// const stompClientInit = null;
const stompClientInit = over(new WebSocket("ws://localhost:3000"));
// 드래그중인 값
export const websocketSlice = createSlice({
  name: 'websocketValue',
  initialState:{stompClient: stompClientInit, connect: false, fail: false},
  reducers:{
    connect:(state) =>{
      let stompClient = null;
      const Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
      stompClient = over(Sock);
      stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, ()=>{state.connect = true; state.fail=false;}, (error) => {
        console.log(error);
        state.connect = false; state.fail = true;
      });
      state.stompClient = stompClient;
      return stompClient;
    },
    enterRoom:(state, action) =>{
      const subscribes = action.payload.subscribes;
      for(let i=0;i<action.payload.apis.length;i++){
        state.stompClient.subscribe(subscribes[i].api(action.payload.gameId),
          subscribes[i].receiver, (error) => {console.log(error);});
      }
    }
  }
});
export const { connect, enterRoom } = websocketSlice.actions;
export default websocketSlice.actions;