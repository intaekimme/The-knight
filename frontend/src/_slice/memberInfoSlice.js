import React from "react";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import api from "../api/api";
import { create } from "@mui/material/styles/createTransitions";
import GoogleLogin from '../commons/login/GoogleLogin';

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


export const memberInfoSlice = createSlice({
  name: "memberInfoSlice",
  initialState: { memberInfo: memberInfoInit, memberHistory: memberHistoryInit },
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
    [patchMemberInfo.fulfilled]: () => {
      fetchMemberInfo();
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
