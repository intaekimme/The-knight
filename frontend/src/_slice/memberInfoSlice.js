import React from "react";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import api from "../api/api";
import { create } from "@mui/material/styles/createTransitions";
import GoogleLogin from '../commons/login/GoogleLogin';

const memberInfoSliceInit = {
  confirmPassword: false,
  // MemberInfo: {
  //   id: 1,
  //   nickname: '더나이트최강자',
  //   image: 'https://picsum.photos/id/237/200/300',
  //   ranking: 1,
  //   score: 323,
  //   win: 7,
  //   lose: 3,
  //   currentReco: [
  //     {
  //       gameId: 1,
  //       result: '승',
  //       capacity: 10,
  //       sword: 2,
  //       twin: 2,
  //       shield: 2,
  //       hand: 2,
  //       alliance: [
  //         {
  //           image: '',
  //           nickname: '우리팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: '우리팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: '우리팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: '우리팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '더나이트최강자',
  //         },
  //       ],
  //       opposite: [
  //         {
  //           image: '',
  //           nickname: '적팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여5',
  //         },
  //       ],
  //     },
  //     {
  //       gameId: 2,
  //       result: '패',
  //       capacity: 8,
  //       sword: 3,
  //       twin: 1,
  //       shield: 3,
  //       hand: 1,
  //       alliance: [
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '더나이트최강자',
  //         },
  //       ],
  //       opposite: [
  //         {
  //           image: '',
  //           nickname: '적팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여5',
  //         },
  //       ],
  //     },
  //     {
  //       gameId: 1,
  //       result: '승',
  //       capacity: 10,
  //       sword: 2,
  //       twin: 2,
  //       shield: 2,
  //       hand: 2,
  //       alliance: [
  //         {
  //           image: '',
  //           nickname: '우리팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: '우리팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: '우리팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: '우리팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '더나이트최강자',
  //         },
  //       ],
  //       opposite: [
  //         {
  //           image: '',
  //           nickname: '적팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여5',
  //         },
  //       ],
  //     },
  //     {
  //       gameId: 2,
  //       result: '패',
  //       capacity: 6,
  //       sword: 3,
  //       twin: 1,
  //       shield: 3,
  //       hand: 1,
  //       alliance: [
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: 'ㅋ우리팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '더나이트최강자',
  //         },
  //       ],
  //       opposite: [
  //         {
  //           image: '',
  //           nickname: '적팀임여1',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여2',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여3',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여4',
  //         },
  //         {
  //           image: '',
  //           nickname: '적팀임여5',
  //         },
  //       ],
  //     },
  //   ]
  // },
}

const fetchMemberInfo = createAsyncThunk('fetchMemberInfo', async () => {
  try {
    const res = await axios.get(api.getMemberInfo(), {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("member info", res.data);
    return res.data;
  } catch (err) {
    console.log(err);
    if (err.response.status === 401) {
      GoogleLogin();
    }
  }
})

const fetchMemberHistory = createAsyncThunk('fetchMemberHistory', async () => {
  try {
    const res = await axios.get(api.getMemberHistory(), {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("member history", res.data.games);
    return res.data;
  } catch (err) {
    console.log(err);
    if (err.response.status === 401) {
      GoogleLogin();
    }
  }
})

// 멤버 프로필 수정
const patchMemberInfo = createAsyncThunk('patchMemberInfo', async (newInfo) => {
  console.log("patch data", newInfo);
  try {
    await axios.patch(api.updateMemberInfo(), { nickname: newInfo.newNickname, image: newInfo.newUrl }, {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` },
    },
    );
    console.log("update profile success");
  } catch (err) {
    if (err.response.status === 401) {
      GoogleLogin();
    }
    return err;
  }
})

// 멤버 탈퇴
const deleteMemberInfo = createAsyncThunk('deleteMemberInfo', async () => {
  try {
    await axios.delete(api.deleteMember(), {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` },
    });
    console.log("delete member success");
  } catch (err) {
    if (err.response.status === 401) {
      GoogleLogin();
    }
    return err;
  }
})

const memberInfoInit = {
  nickname: '',
  image: '',
  ranking: -1,
  score: -1,
  win: -1,
  lose: -1,
}

const memberHistoryInit = [
  {
    gameId: -1,
    result: "",
    capacity: -1,
    sword: -1,
    twin: -1,
    shield: -1,
    hand: -1,
    alliance: [{
      image: "",
      nickname: ""
    },
    ],
    opposite: [{
      image: "",
      nickname: ""
    }
    ],
  }
]

export const memberInfoSlice = createSlice({
  name: "memberInfoSlice",
  initialState: { value: memberInfoSliceInit, memberInfo: memberInfoInit, memberHistory: memberHistoryInit },
  reducers: {
    changeImage: (state, action) => {
      state.memberInfo.image = action.payload
    },
  },
  extraReducers: {
    [fetchMemberInfo.fulfilled]: (state, action) => {
      state.memberInfo = action.payload;
    },
    [fetchMemberInfo.rejected]: state => {
      state.memberInfo = memberInfoInit;
    },
    [fetchMemberHistory.fulfilled]: (state, action) => {
      state.memberHistory = action.payload;
    },
    [fetchMemberHistory.rejected]: state => {
      state.memberHistory = memberHistoryInit;
    },
    [patchMemberInfo.fulfilled]: (state, action) => {
      state.memberInfo = action.payload;
    },
    [patchMemberInfo.rejected]: state => {
      state.memberInfo = memberInfoInit;
    },
    [deleteMemberInfo.fulfilled]: (state, action) => {
      state.memberInfo = action.payload;
    },
    [deleteMemberInfo.rejected]: state => {
      state.memberInfo = memberInfoInit;
    },
  }
});

export { fetchMemberInfo, fetchMemberHistory, patchMemberInfo, deleteMemberInfo };
export const { changeImage } = memberInfoSlice.actions;
export default memberInfoSlice.reducer;
