import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const playersInit = {
  peopleNum: 3,
  teamA: {
    player1: {
      memberId: 1,
      nickname: 'John',
      leftCount: 0,
      rightCount: 0,
      isMine: true,
      weapons: [null, null],
    },
    player2: {
      memberId: 2,
      nickname: 'Smith',
      leftCount: 0,
      rightCount: 0,
      isMine: false,
      weapons: [null, null],
    },
    player3: {
      memberId: 3,
      nickname: 'Sara',
      leftCount: 0,
      rightCount: 0,
      isMine: false,
      weapons: [null, null],
    },
  },
  teamB: {
    player1: {
      memberId: 4,
      nickname: 'Bob',
      leftCount: 0,
      rightCount: 0,
      isMine: false,
      weapons: [null, null],
    },
    player2: {
      memberId: 5,
      nickname: 'Tom',
      leftCount: 0,
      rightCount: 0,
      isMine: false,
      weapons: [null, null],
    },
    player3: {
      memberId: 6,
      nickname: 'Ria',
      leftCount: 0,
      rightCount: 0,
      isMine: false,
      weapons: [null, null],
    },
  },
}

const orderInit = {
  teamA: [null, null, null, null, null],
  teamB: [null, null, null, null, null],
}

// 0: 무기선택, 1: 공/수 선택, 2: 애니메이션, 3: 의심, 4: 게임종료
const phaseInit = 0

export const gameSlice = createSlice({
  name: "gameSlice",
  initialState: {
    players: playersInit,
    order: orderInit,
    phase: phaseInit,
  },
  reducers: {
    fetchPlayers: (state, action) => {
      state.players = action.payload
    },
    selectWeapon: (state, action) => {
      // 지금은 A팀 플레이어1의 무기를 바꾸는 것으로 고정
      // 추후에 해당 유저의 무기를 바꾸는 것으로 변경
      if (!state.players.teamA.player1.weapons[0]) {
        state.players.teamA.player1.weapons[0] = action.payload
      } else if (!state.players.teamA.player1.weapons[1]) {
        state.players.teamA.player1.weapons[1] = action.payload
      }
    },
    deleteWeapon: (state, action) => {
      if (action.payload === 'left') {
        state.players.teamA.player1.weapons[0] = null
      } else if (action.payload === 'right') {
        state.players.teamA.player1.weapons[1] = null
      }
    },
    selectOrder: (state, action) => {
      // 지금은 A팀 플레이어1의 무기를 바꾸는 것으로 고정
      // 추후에 해당 유저의 무기를 바꾸는 것으로 변경
      // 선택한 순서가 비어있고, 유저가 이미 선택한 순서가 없을 때
      if (!state.order.teamA[action.payload - 1]) {
        for (let i=0; i < state.order.teamA.length; i++) {
          // 선택한 순서가 비어있고, (다른 순서를 이미 선택해뒀을 때)
          if (state.order.teamA[i]) {
            if (state.order.teamA[i].memberId === state.players.teamA.player1.memberId) {
              state.order.teamA[i] = null
            }
          }
        }
        state.order.teamA[action.payload - 1] = state.players.teamA.player1
        // (선택한 순서가 차있고), 유저가 선택했던 순서일 때
      } else if (state.order.teamA[action.payload - 1].memberId === state.players.teamA.player1.memberId) {
        state.order.teamA[action.payload - 1] = null
      }
    }
  },
});
export const { fetchPlayers, selectWeapon, deleteWeapon, selectOrder } = gameSlice.actions;
export default gameSlice.reducer;
