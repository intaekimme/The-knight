import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const playersSliceInit = [
  {
    user: {
      userName: "player1",
    },
    team: "A",
  },
  {
    user: {
      userName: "player2",
    },
    team: "A",
  },
  {
    user: {
      userName: "player3",
    },
    team: "A",
  },
  {
    user: {
      userName: "player4",
    },
    team: "B",
  },
  {
    user: {
      userName: "player5",
    },
    team: "B",
  },
  {
    user: {
      userName: "player6",
    },
    team: "B",
  },
];

export const playersSlice = createSlice({
  name: "playersSlice",
  initialState: { value: playersSliceInit },
  reducers: {},
});
export default playersSlice.reducer;
