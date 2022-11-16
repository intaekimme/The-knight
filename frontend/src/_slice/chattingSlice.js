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
const tempCattingData = [{
  memberId: 1,
  nickname: "1번사람",
  content: "안녕하세요",
  chattingSet: "ALL"
},{
  memberId: 2,
  nickname: "2번사람",
  content: "안녕하세요 두번째 챗",
  chattingSet: "ALL"
},{
  memberId: 3,
  nickname: "3번사람",
  content: "안녕하세요 A 챗",
  chattingSet: "A"
},{
  memberId: 4,
  nickname: "4번사람",
  content: "안녕하세요 B 챗",
  chattingSet: "B"
},
{
  memberId: 1,
  nickname: "1번사람",
  content: "안녕하세요",
  chattingSet: "ALL"
},{
  memberId: 2,
  nickname: "2번사람",
  content: "안녕하세요 두번째 챗",
  chattingSet: "ALL"
},{
  memberId: 3,
  nickname: "3번사람",
  content: "안녕하세요 A 챗",
  chattingSet: "A"
},{
  memberId: 4,
  nickname: "4번사람",
  content: "안녕하세요 B 챗",
  chattingSet: "B"
},{
  memberId: 1,
  nickname: "1번사람",
  content: "안녕하세요",
  chattingSet: "ALL"
},{
  memberId: 2,
  nickname: "2번사람",
  content: "안녕하세요 두번째 챗",
  chattingSet: "ALL"
},{
  memberId: 3,
  nickname: "3번사람",
  content: "안녕하세요 A 챗",
  chattingSet: "A"
},{
  memberId: 4,
  nickname: "4번사람",
  content: "안녕하세요 B 챗",
  chattingSet: "B"
},
];
export const chattingSlice = createSlice({
  name: 'chatting',
  initialState:{chatting: tempCattingData},
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