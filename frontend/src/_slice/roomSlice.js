import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import api from '../api/api';

const initRoom = createAsyncThunk('room/initRoom', async (props, { rejectWithValue }) => {
  try {
    const res = await axios.post(`${api.initRoom()}`, props.roomInfo, {
      headers: {Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}
    });
    console.log("방 생성 성공", res);
    return {gameId: res.data.gameId};
  } catch (err) {
    console.log(props.roomInfo);
    console.log("방 생성 실패", err);
    return rejectWithValue(err.response.data);
  }
});

const roomInfo = createAsyncThunk('roomInfo', async (gameId, { rejectWithValue }) => {
  try {
    const res = await axios.get(api.gameRoomInfo(gameId), {});
    console.log(res.data);
    return res.data;
  } catch (err) {
    return rejectWithValue(err.response.data);
  }
});
const usersInfo = createAsyncThunk('usersInfo', async (gameId, { rejectWithValue }) => {
  try {
    const res = await axios.get(api.gameMembersInfo(gameId), {});
    console.log(res.data);
    return res.data;
  } catch (err) {
    return rejectWithValue(err.response.data);
  }
});

const roomInit = {
  state : -1,
  gameId: -1,
  title: "테스트 제목",
  maxMember: 4,
  // currentUser: -1,
  sword: 0,
  twin: 0,
  shield: 0,
  hand: 0,
}
const userInit = [{
  id: -1,
  nickname: "닉네임",
  image: "url",
  team: "A", // A/B
  readyStatus: false,
},{
  id: -1,
  nickname: "닉네임",
  image: "url",
  team: "A", // A/B
  readyStatus: false,
},{
  id: -1,
  nickname: "닉네임",
  image: "url",
  team: "A", // A/B
  readyStatus: false,
},{
  id: -1,
  nickname: "닉네임",
  image: "url",
  team: "A", // A/B
  readyStatus: false,
},];
export const roomSlice = createSlice({
  name: 'room',
  initialState:{roomInfo: roomInit, usersInfo: userInit},
  reducers:{
    modifyRoomSetting:(state, action) =>{
      state.roomInfo.title = action.payload.title;
      state.roomInfo.maxUser = action.payload.maxUser;
      state.roomInfo.sword = action.payload.sword;
      state.roomInfo.twin = action.payload.twin;
      state.roomInfo.shield = action.payload.shield;
      state.roomInfo.hand = action.payload.hand;
    },
    setState:(state, action) =>{
      state.roomInfo.state = action.payload.state;
    },
    setMembers:(state, action) =>{
      console.log(action);
      state.usersInfo = [...action.payload];
    },
    changeTeam:(state, action) =>{
      const tempUsersInfo = state.usersInfo;
      for(let i=0;i<tempUsersInfo.length;i++){
        if(tempUsersInfo[i].id === action.payload.memberId){
          tempUsersInfo[i].team = action.payload.team;
          break;
        }
      }
      state.usersInfo = tempUsersInfo;
    },
    changeReady:(state, action) =>{
      const tempUsersInfo = state.usersInfo;
      for(let i=0;i<tempUsersInfo.length;i++){
        if(tempUsersInfo[i].id === action.payload.memberId){
          tempUsersInfo[i].readyStatus = action.payload.readyStatus;
          break;
        }
      }
      state.usersInfo = tempUsersInfo;
    },
  },
  extraReducers: {
    [initRoom.fulfilled]: (state, action) => {
      state.roomInfo.gameId = action.payload.gameId;
    },
    [initRoom.rejected]: state => {
      state.roomInfo = roomInit;
    },
    [roomInfo.fulfilled]: (state, action) => {
      state.roomInfo = action.payload;
    },
    [roomInfo.rejected]: state => {
      state.roomInfo = roomInit;
    },
    [usersInfo.fulfilled]: (state, action) => {
      state.usersInfo = [...action.payload];
    },
    [usersInfo.rejected]: state => {
      state.usersInfo = userInit;
    },
  },
});

export { initRoom, roomInfo, usersInfo };
export const { modifyRoomSetting, setState, setMembers, changeTeam, changeReady } = roomSlice.actions;
export default roomSlice.reducer;