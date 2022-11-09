import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const meInit = {
  memberId: 5,
  nickname: "Tom",
  index: 4,
  team: "B",
  pass: false,
}

const playersInit = {
  maxUser: 6,
  players: [
    {
      memberId: 1,
      nickname: "John",
      leftCount: 0,
      rightCount: 0,
      team: "A",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 2,
      nickname: "Smith",
      leftCount: 0,
      rightCount: 0,
      team: "A",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 3,
      nickname: "Sara",
      leftCount: 0,
      rightCount: 0,
      team: "A",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 4,
      nickname: 'Bob',
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 5,
      nickname: 'Tom',
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 6,
      nickname: 'Ria',
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
  ]
}

const orderInit = {
  A: [null, null, null, null, null],
  B: [null, null, null, null, null],
}

// PREPARE, PREDECESSOR, ATTACK, ATTACK_DOUBT, DEFEND, DEFEND_DOUBT, DOUBT_RESULT, EXECUTE, RESULT, END
const phaseInit = "DEFEND_DOUBT"
const previousPhaseInit = null

const isLoadingInit = false

const currentAttackerInit = {
  memberId: 1,
  nickname: "John",
  leftCount: 0,
  rightCount: 0,
  team: "A",
  order: 0,
  weapons: [null, null],
}

const currentDefenderInit = {
  memberId: 3,
  nickname: "Sara",
  leftCount: 0,
  rightCount: 0,
  team: "A",
  order: 0,
  weapons: [null, null],
}

export const gameSlice = createSlice({
  name: "gameSlice",
  initialState: {
    me: meInit,
    players: playersInit,
    order: orderInit,
    phase: phaseInit,
    previousPhase: previousPhaseInit,
    isLoading: isLoadingInit,
    currentAttacker: currentAttackerInit,
    currentDefender: currentDefenderInit,
  },
  reducers: {
    fetchMe: (state) => {
      const memberId = window.localStorage.getItem("memberId")
      const player = state.players.players.find((player) => player.memberId === memberId)
      const playerIndex = state.players.players.findIndex((player) => player.memberId === memberId)
    
      state.me = {
        memberId: memberId,
        nickname: player.nickname,
        index: playerIndex,
        team: player.team,
      }
    },
    fetchPlayers: (state, action) => {
      state.players = action.payload
    },
    selectWeapon: (state, action) => {
      if (!state.players.players[state.me.index].weapons[0]) {
        state.players.players[state.me.index].weapons[0] = action.payload
      } else if (!state.players.players[state.me.index].weapons[1]) {
        state.players.players[state.me.index].weapons[1] = action.payload
      }
    },
    deleteWeapon: (state, action) => {
      if (action.payload === 'left') {
        state.players.players[state.me.index].weapons[0] = null
      } else if (action.payload === 'right') {
        state.players.players[state.me.index].weapons[1] = null
      }
    },
    selectOrder: (state, action) => {
      // 선택한 순서가 비어있고, 유저가 이미 선택한 순서가 없을 때
      if (!state.order[state.me.team][action.payload]) {
        for (let i=0; i < state.order[state.me.team].length; i++) {
          // 선택한 순서가 비어있고, (다른 순서를 이미 선택해뒀을 때)
          if (state.order[state.me.team][i]) {
            if (state.order[state.me.team][i].memberId === state.players.players[state.me.index].memberId) {
              state.order[state.me.team][i] = null
            }
          }
        }
        state.order[state.me.team][action.payload] = state.players.players[state.me.index]
        // (선택한 순서가 차있고), 유저가 선택했던 순서일 때
      } else if (state.order[state.me.team][action.payload].memberId === state.players.players[state.me.index].memberId) {
        state.order[state.me.team][action.payload] = null
      }
    },
    fetchPhase: (state, action) => {
      state.previousPhase = state.phase
      state.phase = action.payload
    },
    switchIsLoading: (state) => {
      state.isLoading = !state.isLoading
    },
    selectPass: (state) => {
      state.me.pass = true
    },
    initializePass: (state) => {
      state.me.pass = false
    },
  },
});
export const { fetchMe, fetchPlayers, selectWeapon, deleteWeapon, selectOrder, fetchPhase, switchIsLoading, selectPass, initializePass } = gameSlice.actions;
export default gameSlice.reducer;
