import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
// import axios from 'axios';
// import api from '../api/api';

// const tempCattingData = [{
//   memberId: 1,
//   nickname: "1번사람",
//   content: "안녕하세요",
//   chattingSet: "ALL"
// }];
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