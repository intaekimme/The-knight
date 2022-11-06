import React from "react";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import api from "../api/api";

const memberInfoSliceInit = {
  confirmPassword: false,
  MemberInfo: {
    id: 1,
    nickname: '더나이트최강자',
    image: 'https://picsum.photos/id/237/200/300',
    ranking: 1,
    score: 323,
    win: 7,
    lose: 3,
    currentReco: [
      {
        gameId: 1,
        result: '승',
        capacity: 10,
        sword: 2,
        twin: 2,
        shield: 2,
        hand: 2,
        alliance: [
          {
            image: '',
            nickname: '우리팀임여1',
          },
          {
            image: '',
            nickname: '우리팀임여2',
          },
          {
            image: '',
            nickname: '우리팀임여3',
          },
          {
            image: '',
            nickname: '우리팀임여4',
          },
          {
            image: '',
            nickname: '더나이트최강자',
          },
        ],
        opposite: [
          {
            image: '',
            nickname: '적팀임여1',
          },
          {
            image: '',
            nickname: '적팀임여2',
          },
          {
            image: '',
            nickname: '적팀임여3',
          },
          {
            image: '',
            nickname: '적팀임여4',
          },
          {
            image: '',
            nickname: '적팀임여5',
          },
        ],
      },
      {
        gameId: 2,
        result: '패',
        capacity: 8,
        sword: 3,
        twin: 1,
        shield: 3,
        hand: 1,
        alliance: [
          {
            image: '',
            nickname: 'ㅋ우리팀임여1',
          },
          {
            image: '',
            nickname: 'ㅋ우리팀임여2',
          },
          {
            image: '',
            nickname: 'ㅋ우리팀임여3',
          },
          {
            image: '',
            nickname: 'ㅋ우리팀임여4',
          },
          {
            image: '',
            nickname: '더나이트최강자',
          },
        ],
        opposite: [
          {
            image: '',
            nickname: '적팀임여1',
          },
          {
            image: '',
            nickname: '적팀임여2',
          },
          {
            image: '',
            nickname: '적팀임여3',
          },
          {
            image: '',
            nickname: '적팀임여4',
          },
          {
            image: '',
            nickname: '적팀임여5',
          },
        ],
      },
      {
        gameId: 1,
        result: '승',
        capacity: 10,
        sword: 2,
        twin: 2,
        shield: 2,
        hand: 2,
        alliance: [
          {
            image: '',
            nickname: '우리팀임여1',
          },
          {
            image: '',
            nickname: '우리팀임여2',
          },
          {
            image: '',
            nickname: '우리팀임여3',
          },
          {
            image: '',
            nickname: '우리팀임여4',
          },
          {
            image: '',
            nickname: '더나이트최강자',
          },
        ],
        opposite: [
          {
            image: '',
            nickname: '적팀임여1',
          },
          {
            image: '',
            nickname: '적팀임여2',
          },
          {
            image: '',
            nickname: '적팀임여3',
          },
          {
            image: '',
            nickname: '적팀임여4',
          },
          {
            image: '',
            nickname: '적팀임여5',
          },
        ],
      },
      {
        gameId: 2,
        result: '패',
        capacity: 6,
        sword: 3,
        twin: 1,
        shield: 3,
        hand: 1,
        alliance: [
          {
            image: '',
            nickname: 'ㅋ우리팀임여1',
          },
          {
            image: '',
            nickname: 'ㅋ우리팀임여2',
          },
          {
            image: '',
            nickname: 'ㅋ우리팀임여3',
          },
          {
            image: '',
            nickname: 'ㅋ우리팀임여4',
          },
          {
            image: '',
            nickname: '더나이트최강자',
          },
        ],
        opposite: [
          {
            image: '',
            nickname: '적팀임여1',
          },
          {
            image: '',
            nickname: '적팀임여2',
          },
          {
            image: '',
            nickname: '적팀임여3',
          },
          {
            image: '',
            nickname: '적팀임여4',
          },
          {
            image: '',
            nickname: '적팀임여5',
          },
        ],
      },
    ]
  },
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

export const memberInfoSlice = createSlice({
  name: "memberInfoSlice",
  initialState: { value: memberInfoSliceInit },
  reducers: {
    changeImage: (state, action) => {
      state.value.MemberInfo.image = action.payload
    },
  },
  extraReducers: {
    [fetchMemberInfo.fulfilled]: (state, action) => {
      state.fetchMemberInfo = action.payload;
    },
    [fetchMemberInfo.rejected]: state => {
      state.fetchMemberInfo = memberInfoInit;
    },
  }
});

export { fetchMemberInfo };
export const { changeImage } = memberInfoSlice.actions;
export default memberInfoSlice.reducer;
