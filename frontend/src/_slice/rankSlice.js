import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../api/api";
import axios from 'axios';
import GoogleLogin from '../commons/login/GoogleLogin';

const fetchRankAll = createAsyncThunk('fetchRankAll', async () => {
  try {
    const res = await axios.get(api.getRankList(), {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    return res.data;
  } catch (err) {
    if (err.response.status === 401) {
      GoogleLogin();
    }
    return err;
  }
})
const searchRank = createAsyncThunk('searchRank', async (keyword) => {
  try {
    const res = await axios.get(api.getRankList() + `?keyword=${keyword}`, {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("get searched rank", res.data);
    return res.data;
  } catch (err) {
    if (err.response.status === 401) {
      GoogleLogin();
    }
    return err;
  }
})

const rankListInit = [
  {
    nickname: '',
    image: '',
    score: -1,
    ranking: -1
  },
]

export const rankSlice = createSlice({
  name: "rankSlice",
  initialState: { rankList: rankListInit },
  reducers: {
  },
  extraReducers: {
    [fetchRankAll.fulfilled]: (state, action) => {
      state.rankList = action.payload.rankings;
    },
    [fetchRankAll.rejected]: state => {
      state.rankList = rankListInit;
    },
    [searchRank.fulfilled]: (state, action) => {
      state.rankList = action.payload.rankings;
    },
    [searchRank.rejected]: state => {
      state.rankList = rankListInit;
    },
  },
});
export { fetchRankAll, searchRank };
export default rankSlice.reducer;