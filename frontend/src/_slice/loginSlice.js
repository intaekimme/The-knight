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
const loginInit = {
  isLogin: false,
  token: "",
}
export const loginSlice = createSlice({
  name: 'loginValue',
  initialState:{value: loginInit},
  reducers:{
    login:(state, action) =>{
      state.value.isLogin = true;
      state.value.token = action.payload;
    },
    logout:(state) =>{
      state.value.isLogin = false;
      state.value.token = "";
    }
  },
  extraReducers:{

  }
});
export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;