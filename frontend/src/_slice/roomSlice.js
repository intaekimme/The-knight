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
export { roomInfo };
 const roomInit = {
  gameId: -1,
  title: "",
  capacity: -1,
  participant: -1,
  sword: -1,
  twin: -1,
  shield: -1,
  hand: -1,
}
export const roomSlice = createSlice({
  name: 'roomValue',
  initialState:{value: roomInit},
  reducers:{
    setRoom:(state, action) =>{
      state.value.gameId = action.payload.gameId;
      state.value.title = action.payload.title;
      state.value.capacity = action.payload.capacity;
      state.value.participant = action.payload.participant;
      state.value.sword = action.payload.sword;
      state.value.twin = action.payload.twin;
      state.value.shield = action.payload.shield;
      state.value.hand = action.payload.hand;
    },
  },
  extraReducers: {
    [roomInfo.fulfilled]: (state, action) => {
      state.value = action.payload;
    },
    [roomInfo.rejected]: state => {
      state.value = roomInit;
    },
  },
});
export const { setRoom } = roomSlice.actions;
export default roomSlice.reducer;