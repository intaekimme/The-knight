import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const meInit = {
  memberId: 5,
  nickname: "Tom",
  index: 4,
  team: "B",
  pass: false,
};

const timerInit = {
  timer: 0,
  intervalObject: null,
};

const playersInit = {
  state: "",
  maxMember: 6,
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
      nickname: "Bob",
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 5,
      nickname: "Tom",
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 6,
      nickname: "Ria",
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
  ],
};

const leaderInit = {
  A: null,
  B: null
}

const attackFirstInit = ""

const orderInit = {
  A: [null, null, null, null, null],
  B: [null, null, null, null, null],
};

const countWeaponInit = {
  A: {},
  B: {},
}

const isSelectCompleteInit = {
  A: false,
  B: false,
}

// PREPARE, PREDECESSOR, ATTACK, ATTACK_DOUBT, DEFENSE, DEFENSE_DOUBT, DOUBT_RESULT, EXECUTE(공&방 결과), END
const phaseInit = "EXECUTE";
const previousPhaseInit = null;

const isLoadingInit = false;

const currentAttackerInit = {
  memberId: 1,
  team: "A",
};

const currentDefenserInit = {
  memberId: 3,
  team: "A",
};

const attackInfoInit = {
  attacker: {},
  defenser: {},
  weapon: "",
  hand: "",
}

const defenseInfoInit = {
  defenser: {},
  weapon: "",
  hand: "",
}

const endInfoInit = {
  isWin: true,
  losingteam: "B",
  losingLeaderId: null,
  winningLeaderId: null,
  players: [],
};

export const gameSlice = createSlice({
  name: "gameSlice",
  initialState: {
    me: meInit,
    timer: timerInit,
    players: playersInit,
    leader: leaderInit,
    attakFirst: attackFirstInit,
    order: orderInit,
    countweapon: countWeaponInit,
    isSelectComplete: isSelectCompleteInit,
    phase: phaseInit,
    previousPhase: previousPhaseInit,
    isLoading: isLoadingInit,
    currentAttacker: currentAttackerInit,
    currentDefenser: currentDefenserInit,
    attackInfo: attackInfoInit,
    defenseInfo: defenseInfoInit,
    endInfo: endInfoInit,
  },
  reducers: {
    setMe: (state) => {
      const memberId = window.localStorage.getItem("memberId");
      const player = state.players.players.find((player) => player.memberId === memberId);
      const playerIndex = state.players.players.findIndex((player) => player.memberId === memberId);

      state.me = {
        memberId: memberId,
        nickname: player.nickname,
        index: playerIndex,
        team: player.team,
      };
    },
    setTimer: (state, action) => {
      state.timer.timer = action.payload;
    },
    countTimer: (state, action) => {
      state.timer.timer = state.timer.timer - 1;
      state.timer.intervalObject = action.payload;
    },
    stopCountTimer: (state) => {
      if (state.timer.timer <= 0) {
        state.timer.timer = 0;
        clearInterval(state.timer.intervalObject);
      }
    },
    fetchPlayers: (state, action) => {
      state.players = action.payload;
    },
    selectWeapon: (state, action) => {
      if (!state.players.players[state.me.index].weapons[0]) {
        state.players.players[state.me.index].weapons[0] = action.payload;
      } else if (!state.players.players[state.me.index].weapons[1]) {
        state.players.players[state.me.index].weapons[1] = action.payload;
      }
    },
    deleteWeapon: (state, action) => {
      if (action.payload === "left") {
        state.players.players[state.me.index].weapons[0] = null;
      } else if (action.payload === "right") {
        state.players.players[state.me.index].weapons[1] = null;
      }
    },
    selectOrder: (state, action) => {
      // 선택한 순서가 비어있고, 유저가 이미 선택한 순서가 없을 때
      if (!state.order[state.me.team][action.payload]) {
        for (let i = 0; i < state.order[state.me.team].length; i++) {
          // 선택한 순서가 비어있고, (다른 순서를 이미 선택해뒀을 때)
          if (state.order[state.me.team][i]) {
            if (
              state.order[state.me.team][i].memberId ===
              state.players.players[state.me.index].memberId
            ) {
              state.order[state.me.team][i] = null;
            }
          }
        }
        state.order[state.me.team][action.payload] = state.players.players[state.me.index];
        // (선택한 순서가 차있고), 유저가 선택했던 순서일 때
      } else if (
        state.order[state.me.team][action.payload].memberId ===
        state.players.players[state.me.index].memberId
      ) {
        state.order[state.me.team][action.payload] = null;
      }
    },
    fetchOrder: (state, action) => {
      state.order[state.me.team] = action.payload;
    },
    fetchPhase: (state, action) => {
      state.previousPhase = state.phase;
      state.phase = action.payload;
    },
    switchIsLoading: (state) => {
      state.isLoading = !state.isLoading;
    },
    selectPass: (state) => {
      state.me.pass = true;
    },
    initializePass: (state) => {
      state.me.pass = false;
    },
    setEndInfo: (state, action) => {
      state.endInfo = action.payload;
    },
    setLeader: (state, action) => {
      state.leader[state.me.team] = action.payload;
    },
    setAttackFirst: (state, action) => {
      state.attackFirst = action.payload;
    },
    fetchCountWeapon: (state, action) => {
      state.countweapon[state.me.team] = action.payload;
    },
    selectComplete: (state) => {
      state.isSelectComplete[state.me.team] = true;
    },
    fetchCurrentAttacker: (state, action) => {
      state.currentAttacker = action.payload;
    },
    fetchCurrentDefenser: (state, action) => {
      state.currentDefenser = action.payload;
    },
    fetchAttackInfo: (state, action) => {
      state.attackInfo = action.payload;
    },
    fetchDefenseInfo: (state, action) => {
      state.defenseInfo = action.payload;
    },
  },
});
export const {
  setMe,
  setTimer,
  countTimer,
  stopCountTimer,
  fetchPlayers,
  selectWeapon,
  deleteWeapon,
  selectOrder,
  fetchOrder,
  fetchPhase,
  switchIsLoading,
  selectPass,
  initializePass,
  setEndInfo,
  setLeader,
  setAttackFirst,
  fetchCountWeapon,
  selectComplete,
  fetchCurrentAttacker,
  fetchCurrentDefenser,
  fetchAttackInfo,
  fetchDefenseInfo,
} = gameSlice.actions;
export default gameSlice.reducer;
