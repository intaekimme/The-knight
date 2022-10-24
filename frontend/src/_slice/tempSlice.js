import React from "react";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
// import axios from 'axios';
// import api from '../api/api';

const tempValueInit = {
  a: "a",
  b: "b",
}
// 드래그중인 값
export const tempValue = createSlice({
  name: 'tempValue',
  initialState:{value: tempValueInit},
  reducers:{
    setValueA:(state, action) =>{
      state.value.a = action.payload;
    },
    setValueB:(state, action) =>{
      state.value.b = action.payload;
    }
  }
});
export const tempValueAction = tempValue.actions;