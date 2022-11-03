import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { over, Client } from "stompjs";
import SockJS from "sockjs-client";
// import axios from 'axios';
// import api from '../api/api';

let stompClient = null;
const Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
stompClient = over(Sock);
stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, (error) => {
  console.log(error);
});

const stompClientInit = new Client();
// 드래그중인 값
export const websocketSlice = createSlice({
  name: 'websocketValue',
  initialState:{stompClient: stompClientInit},
  reducers:{
    connect:(state) =>{
      let stompClient = null;
      const Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
      stompClient = over(Sock);
      stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, ()=>{}, (error) => {
        console.log(error);
      });
      state.stompClient = stompClient;
    },
    enterRoom:(state, action) =>{
      state.stompClient.subscribe(api.enterRoom(action.payload), onMessageReceived, (error) => {
        console.log(error);
      });
      state.stompClient.subscribe(api.allMembersInRoom(action.payload), onMessageReceived, (error) => {
        console.log(error);
      });
      state.stompClient.subscribe(api.exitRoom(action.payload), onMessageReceived, (error) => {
        console.log(error);
      });
      state.stompClient.subscribe(api.selectTeam(action.payload), onMessageReceived, (error) => {
        console.log(error);
      });
      state.stompClient.subscribe(api.ready(action.payload), onMessageReceived, (error) => {
        console.log(error);
      });
    }
  }
});
export const { setRoom, setUsers } = websocketSlice.actions;
export const websocketAction = websocketSlice.actions;