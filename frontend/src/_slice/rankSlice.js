import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../api/api";
import axios from 'axios';

const fetchRankAll = createAsyncThunk('fetchRankAll', async () => {
  try {
    const res = await axios.get(api.getRankList(), {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("get rank list", res.data.rankings);
    return res.data;
  } catch (err) {
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
  initialState: { fetchRankAll: rankListInit },
  reducers: {
  },
  extraReducers: {
    [fetchRankAll.fulfilled]: (state, action) => {
      state.gameListAll = action.payload.rankings;
    },
    [fetchRankAll.rejected]: state => {
      state.gameListAll = rankListInit;
    },
  },
});
export { fetchRankAll };
export default rankSlice.reducer;