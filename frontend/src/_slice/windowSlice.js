import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
// import axios from 'axios';
// import api from '../api/api';

// const login = createAsyncThunk('login', async (payload, { rejectWithValue }) => {
//   try {
//     const res = await axios.get(api.login());
//     return res.data;
//   } catch (err) {
//     return rejectWithValue(err.response.data);
//   }
// });
// export {login};

// login
const windowInit = {
  width: window.innerWidth,
  height: window.innerHeight,
  mainGridWidth: 10,
  chatGridWidth: 2,
}
export const windowSlice = createSlice({
  name: 'windowValue',
  initialState:{value: windowInit},
  reducers:{
    resize:(state, action) =>{
      state.value.width = action.payload.width;
      state.value.height = action.payload.height;
      if (action.payload.width >= 1000) {
        state.value.mainGridWidth = 10;
        state.value.chatGridWidth = 2;
      }
      else {
        state.value.mainGridWidth = 12;
        state.value.chatGridWidth = 12;
      }
    },
  },
  extraReducers:{

  }
});
export const { resize } = windowSlice.actions;
export default windowSlice.reducer;