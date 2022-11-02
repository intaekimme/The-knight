import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import api from '../api/api';

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
export { roomInfo, usersInfo };
const roomInit = {
  gameId: -1,
  title: "",
  maxUser: -1,
  capacity: -1,
  participant: -1,
  sword: -1,
  twin: -1,
  shield: -1,
  hand: -1,
}
const userInit = [{
  memberId: -1,
  nickname: "닉네임",
  team: "A", // A/B
},{
  memberId: -1,
  nickname: "닉네임",
  team: "A", // A/B
},{
  memberId: -1,
  nickname: "닉네임",
  team: "B", // A/B
},{
  memberId: -1,
  nickname: "닉네임",
  team: "B", // A/B
}];
export const roomSlice = createSlice({
  name: 'roomValue',
  initialState:{roomInfo: roomInit, usersInfo: userInit},
  reducers:{
    setRoom:(state, action) =>{
      state.roomInfo = action.payload;
    },
    setUsers:(state, action) =>{
      state.usersInfo = [...action.payload];
    },
  },
  extraReducers: {
    [roomInfo.fulfilled]: (state, action) => {
      state.value = action.payload;
    },
    [roomInfo.rejected]: state => {
      state.value = roomInit;
    },
    [usersInfo.fulfilled]: (state, action) => {
      state.value = action.payload;
    },
    [usersInfo.rejected]: state => {
      state.value = userInit;
    },
  },
});
export const { setRoom, setUsers } = roomSlice.actions;
export default roomSlice.reducer;