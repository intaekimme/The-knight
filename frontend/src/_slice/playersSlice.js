import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const playersSliceInit = {
  peopleNum: 3,
  teamA: {
    player1: {
      memberId: 1,
      nickname: 'John',
      leftCount: 0,
      rightCount: 0,
      weapons: [null, null],
    },
    player2: {
      memberId: 2,
      nickname: 'Smith',
      leftCount: 0,
      rightCount: 0,
      weapons: [null, null],
    },
    player3: {
      memberId: 3,
      nickname: 'Sara',
      leftCount: 0,
      rightCount: 0,
      weapons: [null, null],
    },
  },
  teamB: {
    player1: {
      memberId: 4,
      nickname: 'Bob',
      leftCount: 0,
      rightCount: 0,
      weapons: [null, null],
    },
    player2: {
      memberId: 5,
      nickname: 'Tom',
      leftCount: 0,
      rightCount: 0,
      weapons: [null, null],
    },
    player3: {
      memberId: 6,
      nickname: 'Ria',
      leftCount: 0,
      rightCount: 0,
      weapons: [null, null],
    },
  },
}

export const playersSlice = createSlice({
  name: "playersSlice",
  initialState: { value: playersSliceInit },
  reducers: {
    selectWeapon: (state, action) => {
      // 지금은 A팀 플레이어1의 무기를 바꾸는 것으로 고정
      // 추후에 해당 유저의 무기를 바꾸는 것으로 변경
      if (!state.value.teamA.player1.weapons[0]) {
        state.value.teamA.player1.weapons[0] = action.payload
      } else if (!state.value.teamA.player1.weapons[1]) {
        state.value.teamA.player1.weapons[1] = action.payload
      }
    },
    deleteWeapon: (state, action) => {
      if (action.payload === 'left') {
        state.value.teamA.player1.weapons[0] = null
      } else if (action.payload === 'right') {
        state.value.teamA.player1.weapons[1] = null
      }
    }
  },
});
export const { selectWeapon, deleteWeapon } = playersSlice.actions;
export default playersSlice.reducer;
