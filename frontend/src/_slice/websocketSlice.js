import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { over, Client } from "stompjs";
import SockJS from "sockjs-client";
// import axios from 'axios';
import api from '../api/api';

const connectWebsocket = createAsyncThunk('connectWebsocket', async (props, { rejectWithValue }) => {
  try {
    let stompClient = null;
    let connect = false;
    let fail = false;
    const Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
    stompClient = over(Sock);
    stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}
      ,()=>{connect = true; fail=false;}
      ,(error) => { console.log(error); connect = false; fail = true;});
    console.log("connect 성공");
    const payload = { stompClient:stompClient, connect:connect, fail:fail};
    return payload;
  } catch (err) {
    console.log(props);
    console.log("connect 실패", err);
    return rejectWithValue(err);
  }
});

const enterRoomSub = createAsyncThunk('enterRoomSub', async (props, { rejectWithValue }) => {
  try {
    const subscribes = props.subscribes;
    for(let i=0;i<props.apis.length;i++){
      props.stompClient.subscribe(subscribes[i].api(props.gameId),
        subscribes[i].receiver, (error) => {console.log(error);});
    }
    console.log("room subscribe init 성공");
    return {navigate: props.navigate, url: props.url};
  } catch (err) {
    console.log(props);
    console.log("room subscribe init 실패", err);
    return rejectWithValue(err);
  }
});

// const stompClientInit = null;
const stompClientInit = over(new WebSocket("ws://localhost:3000"));
const connectInit = false;
const failInit = false;
// 드래그중인 값
export const websocketSlice = createSlice({
  name: 'websocketValue',
  initialState:{stompClient: stompClientInit, connect: connectInit, fail: failInit},
  reducers:{
    // connect:(state) =>{
    //   let stompClient = null;
    //   const Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
    //   stompClient = over(Sock);
    //   stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, ()=>{state.connect = true; state.fail=false;}, (error) => {
    //     console.log(error);
    //     state.connect = false; state.fail = true;
    //   });
    //   state.stompClient = stompClient;
    //   console.log("connect");
    // },
    enterRoom:(state, action) =>{
      console.log(action.payload);
      const subscribes = action.payload.subscribes;
      for(let i=0;i<action.payload.apis.length;i++){
        state.stompClient.subscribe(subscribes[i].api(action.payload.gameId),
          subscribes[i].receiver, (error) => {console.log(error);});
      }
      console.log("abc");
      action.payload.navigate(action.payload.url);
    }
  },
  extraReducers: {
    [connectWebsocket.fulfilled]: (state, action) => {
      state.stompClient = action.payload.stompClient;
      state.connect = action.payload.connect;
      state.fail = action.payload.fail;
    },
    [connectWebsocket.rejected]: state => {
      state.stompClient = stompClientInit;
      state.connect = connectInit;
      state.fail = failInit;
    },
    [enterRoomSub.fulfilled]: (state, action) => {
      const navigate = action.payload.navigate;
      const url = action.payload.url;
      console.log(navigate, url);
      navigate(url);
    },
    [enterRoomSub.rejected]: state => {
      console.log("subscribe error");
    },
  },
});

export { connectWebsocket, enterRoomSub };
export const { connect, enterRoom } = websocketSlice.actions;
export default websocketSlice.actions;