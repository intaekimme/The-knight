import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { over, Client } from "stompjs";
import SockJS from "sockjs-client";
import api from '../api/api';

const connectWebsocket = createAsyncThunk('websocket/connectWebsocket', async (props, { rejectWithValue }) => {
  try {
    let stompClient = null;
    const Sock = new SockJS(`${api.websocket()}?token=${props.token}`);
    stompClient = over(Sock);
    stompClient.connect({Authorization: `Bearer ${props.token}`}
      ,()=>{}
      ,(error) => { console.log(error); });
    console.log("connect 성공");
    return {stompClient: stompClient};
  } catch (err) {
    console.log(props);
    console.log("connect 실패", err);
    return rejectWithValue(err);
  }
});

const enterRoomSubscribe = createAsyncThunk('websocket/enterRoomSubscribe', async (props, { rejectWithValue }) => {
  try {
    const subscribes = props.subscribes;
    for(let i=0;i<subscribes.length;i++){
      props.stompClient.subscribe(subscribes[i].api,
        subscribes[i].receiver, (error) => {console.log(error);});
    }
    console.log("room subscribe init 성공");
    return true;
  } catch (err) {
    console.log(props);
    console.log("room subscribe init 실패", err);
    return rejectWithValue(err);
  }
});

const stompClientInit = over(new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`));
// 드래그중인 값
export const websocketSlice = createSlice({
  name: 'websocket',
  initialState:{stompClient: stompClientInit},
  reducers:{
    setStompClient: (state, action) =>{
      state.stompClient = action.payload;
    },
  },
  extraReducers: {
    [connectWebsocket.fulfilled]: (state, action) => {
      state.stompClient = action.payload.stompClient;
    },
    [connectWebsocket.rejected]: state => {
      const tempClient = over(new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`));
      tempClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, (res)=>{console.log(res, "connect")}, (err)=>{console.log(err)});
      state.stompClient = tempClient;
    },
  },
});

export { connectWebsocket, enterRoomSubscribe };
export const { setStompClient } = websocketSlice.actions;
export default websocketSlice.reducer;