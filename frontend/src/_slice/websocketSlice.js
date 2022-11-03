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
    },
    enterRoom:(state, action) =>{
      const apis = action.payload.apis;
      const receivers = action.payload.recievers;
      for(let i=0;i<action.payload.apis.length;i++){
        state.stompClient.subscribe(apis[i](action.payload.gameId),
          receivers[i], (error) => {console.log(error);});
      }
      // state.stompClient.subscribe(api.enterRoom(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.allMembersInRoom(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.exitRoom(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.selectTeam(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.ready(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
    }
  }
});
export const { connect, enterRoom } = websocketSlice.actions;
export default websocketSlice.actions;