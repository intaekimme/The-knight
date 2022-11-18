import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import api from '../api/api';
import { sanitizeSortModel } from "@mui/x-data-grid/hooks/features/sorting/gridSortingUtils";
import GoogleLogin from '../commons/login/GoogleLogin';

const initRoom = createAsyncThunk('room/initRoom', async (props, { rejectWithValue }) => {
  try {
    const res = await axios.post(`${api.initRoom()}`, props.roomInfo, {
      headers: {Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}
    });
    console.log("방 생성 성공", res);
    return {gameId: res.data.gameId};
  } catch (err) {
    if (err.response.status === 401) {
      GoogleLogin();
    }
    console.log(props.roomInfo);
    console.log("방 생성 실패", err);
    return rejectWithValue(err.response.data);
  }
});

const getRoomInfo = createAsyncThunk('room/getRoomInfo', async (gameId, { rejectWithValue }) => {
  try {
    console.log(api.gameRoomInfo(gameId));
    const res = await axios.get(api.gameRoomInfo(gameId), {headers: {Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}});
    console.log(res.data);
    return res.data;
  } catch (err) {
    if (err.response.status === 401) {
      GoogleLogin();
    }
    console.log(err);
    return rejectWithValue(err.response.data);
  }
});

const getUsersInfo = createAsyncThunk('room/getUsersInfo', async (gameId, { rejectWithValue }) => {
  try {
    const res = await axios.get(api.gameMembersInfo(gameId), {});
    console.log(res.data);
    return res.data;
  } catch (err) {
    return rejectWithValue(err.response.data);
  }
});

const roomInit = {
  // state : -1,
  gameId: -1,
  title: "테스트 제목",
  ownerId: -1,
  maxMember: 4,
  currentMembers: 0,
  sword: 1,
  twin: 1,
  shield: 1,
  hand: 1,
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
    initRoomSetting:(state) =>{
      state.roomInfo = {...roomInit};
    },
    modifyRoomSetting:(state, action) =>{
      const tempRoomData = {...state.roomInfo};
      tempRoomData.title = action.payload.title;
      tempRoomData.maxMember = action.payload.maxMember;
      tempRoomData.sword = action.payload.sword;
      tempRoomData.twin = action.payload.twin;
      tempRoomData.shield = action.payload.shield;
      tempRoomData.hand = action.payload.hand;
      state.roomInfo = tempRoomData;
      console.log(tempRoomData);
    },
    setState:(state, action) =>{
      state.roomInfo.state = action.payload.state;
    },
    setMembers:(state, action) =>{
      console.log(action);
      state.usersInfo = [...action.payload.members];
      state.roomInfo.ownerId = action.payload.ownerId;
    },
    changeTeam:(state, action) =>{
      const tempUsersInfo = [...state.usersInfo];
      for(let i=0;i<tempUsersInfo.length;i++){
        if(tempUsersInfo[i].id === action.payload.memberId){
          tempUsersInfo[i].team = action.payload.team;
          break;
        }
      }
      state.usersInfo = tempUsersInfo;
    },
    changeReady:(state, action) =>{
      const tempUsersInfo = [...state.usersInfo];
      for(let i=0;i<tempUsersInfo.length;i++){
        if(tempUsersInfo[i].id === action.payload.memberId){
          tempUsersInfo[i].readyStatus = action.payload.readyStatus;
          break;
        }
      }
      state.usersInfo = tempUsersInfo;
      if(action.payload.canStart){
        action.payload.navigate(action.payload.url);
      }
    },
  },
  extraReducers: {
    [initRoom.fulfilled]: (state, action) => {
      state.roomInfo.gameId = action.payload.gameId;
    },
    [initRoom.rejected]: state => {
      state.roomInfo = roomInit;
    },
    [getRoomInfo.fulfilled]: (state, action) => {
      state.roomInfo = action.payload;
      console.log(action.payload);
    },
    [getRoomInfo.rejected]: state => {
      state.roomInfo = roomInit;
    },
    [getUsersInfo.fulfilled]: (state, action) => {
      state.usersInfo = [...action.payload];
    },
    [getUsersInfo.rejected]: state => {
      state.usersInfo = userInit;
    },
  },
});

export { initRoom, roomInit, getRoomInfo, getUsersInfo };
export const { initRoomSetting, modifyRoomSetting, setState, setMembers, changeTeam, changeReady } = roomSlice.actions;
export default roomSlice.reducer;