import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const playersSliceInit = [
  {
    user: {
      userName: "player1",
      weapons: [null, null]
    },
    team: "A",
  },
  {
    user: {
      userName: "player2",
      weapons: [null, null]
    },
    team: "A",
  },
  {
    user: {
      userName: "player3",
      weapons: [null, null]
    },
    team: "A",
  },
  {
    user: {
      userName: "player4",
      weapons: [null, null]
    },
    team: "B",
  },
  {
    user: {
      userName: "player5",
      weapons: [null, null]
    },
    team: "B",
  },
  {
    user: {
      userName: "player6",
      weapons: [null, null]
    },
    team: "B",
  },
];

// {
//   team1: {
//     player1: {}
//     player2: {}
//   },
//   team2: {
//     player1: {}
//     player2: {}
//   },
// }

export const playersSlice = createSlice({
  name: "playersSlice",
  initialState: { value: playersSliceInit },
  reducers: {
    selectWeapon: (state, action) => {
      // 지금은 플레이어1의 무기를 바꾸는 것으로 고정
      // 추후에 해당 유저의 무기를 바꾸는 것으로 변경
      if (!state.value[0].user.weapons[0]) {
        state.value[0].user.weapons[0] = action.payload
      } else if (!state.value[0].user.weapons[1]) {
        state.value[0].user.weapons[1] = action.payload
      }
    },
    deleteWeapon: (state, action) => {
      if (action.payload === 'left') {
        state.value[0].user.weapons[0] = null
      } else if (action.payload === 'right') {
        state.value[0].user.weapons[1] = null
      }
    }
  },
});
export const { selectWeapon, deleteWeapon } = playersSlice.actions;
export default playersSlice.reducer;
