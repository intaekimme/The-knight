import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
// import axios from 'axios';
// import api from '../api/api';

// {
//   memberId: long,
//   nickname: String,
//   content: String,
//   chattingSet: String, (All / A / B)
// }
export const chattingSlice = createSlice({
  name: 'chatting',
  initialState:{chatting: []},
  reducers:{
    initChatting: (state) =>{
      state.chatting = [];
    },
    addChatting: (state, action) => {
      state.chatting.push(action.payload);
    }
  },
  extraReducers: {
  },
});

// export {  };
export const { initChatting, addChatting } = chattingSlice.actions;
export default chattingSlice.reducer;