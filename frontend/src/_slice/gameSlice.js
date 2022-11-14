import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

const meInit = {
  memberId: 1,
  nickname: "John",
  index: 0,
  team: "A",
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
    // {
    //   memberId: 4,
    //   nickname: "Bob",
    //   leftCount: 0,
    //   rightCount: 0,
    //   team: "A",
    //   order: 0,
    //   weapons: [null, null],
    // },
    // {
    //   memberId: 5,
    //   nickname: "Tom",
    //   leftCount: 0,
    //   rightCount: 0,
    //   team: "A",
    //   order: 0,
    //   weapons: [null, null],
    // },
    {
      memberId: 6,
      nickname: "Ria",
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 7,
      nickname: "FTX",
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 8,
      nickname: "Sam",
      leftCount: 0,
      rightCount: 0,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    // {
    //   memberId: 9,
    //   nickname: "Sung",
    //   leftCount: 0,
    //   rightCount: 0,
    //   team: "B",
    //   order: 0,
    //   weapons: [null, null],
    // },
    // {
    //   memberId: 10,
    //   nickname: "SSAFY",
    //   leftCount: 0,
    //   rightCount: 0,
    //   team: "B",
    //   order: 0,
    //   weapons: [null, null],
    // },
  ],
};

const leaderInit = 0;

const attackFirstInit = "";

const orderInit = [null, null, null, null, null];

const countWeaponInit = {
  sword: 0,
  twin: 0,
  shield: 0,
  hand: 0,
};

const isSelectCompleteInit = false;

// PREPARE, PREDECESSOR, ATTACK, ATTACK_DOUBT, DEFENSE, DEFENSE_DOUBT, DOUBT_RESULT, EXECUTE(공&방 결과), END
const phaseInit = "PREDECESSOR";
const previousPhaseInit = null;

const isLoadingInit = false;

const currentAttackerInit = {
  memberId: 1,
  team: "A",
};

const currentDefenderInit = {
  memberId: 3,
  team: "A",
};

const attackInfoInit = {
  attacker: {},
  defender: {},
  weapon: "",
  hand: "",
}

const defenseInfoInit = {
  defender: {},
  weapon: "",
  hand: "",
}

const doubtInfoInit =   {
  doubtResponse : {
    suspect : {
      memberId : 0,
      isDead : false,
    },
    suspected : {
      memberId : 0,
      isDead : false,
      weapon: "",
      hand: "",
    },
    doubtTeam: "",
    doubtResult: false,
  },
  doubtStatus : "",
}

const doubtPassListInit = []

const executeInfoInit = {
  attackTeam: "",
  attacker: {
    memberId: 0,
    weapon: "",
    hand: "",
  },
  defender : {
    memberId: 0,
    hand: "",
    isDead: false,
    restCount: 0,
    passedDefense: false,
  }
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
    countWeapon: countWeaponInit,
    isSelectComplete: isSelectCompleteInit,
    phase: phaseInit,
    previousPhase: previousPhaseInit,
    isLoading: isLoadingInit,
    currentAttacker: currentAttackerInit,
    currentDefender: currentDefenderInit,
    attackInfo: attackInfoInit,
    defenseInfo: defenseInfoInit,
    doubtInfo: doubtInfoInit,
    doubtPassList: doubtPassListInit,
    executeInfo: executeInfoInit,
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
    fetchOrder: (state, action) => {
      state.order = action.payload;
    },
    fetchPhase: (state, action) => {
      state.previousPhase = state.phase;
      state.phase = action.payload;
    },
    switchIsLoading: (state) => {
      state.isLoading = !state.isLoading;
    },
    initializePass: (state) => {
      state.doubtPassList = [];
    },
    setEndInfo: (state, action) => {
      state.endInfo = action.payload;
    },
    setLeader: (state, action) => {
      state.leader = action.payload;
    },
    setAttackFirst: (state, action) => {
      state.attackFirst = action.payload;
    },
    fetchCountWeapon: (state, action) => {
      state.countweapon = action.payload;
    },
    selectComplete: (state) => {
      state.isSelectComplete = true;
    },
    fetchCurrentAttacker: (state, action) => {
      state.currentAttacker = action.payload;
    },
    fetchCurrentDefender: (state, action) => {
      state.currentDefender = action.payload;
    },
    fetchAttackInfo: (state, action) => {
      state.attackInfo = action.payload;
    },
    fetchDefenseInfo: (state, action) => {
      state.defenseInfo = action.payload;
    },
    fetchDoubtInfo: (state, action) => {
      state.doubtInfo = action.payload;
    },
    fetchExecuteInfo: (state, action) => {
      state.executeInfo = action.payload;
    },
    addDoubtPass: (state, action) => {
      state.doubtPassList.push(action.payload);
    }
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
  fetchOrder,
  fetchPhase,
  switchIsLoading,
  initializePass,
  setEndInfo,
  setLeader,
  setAttackFirst,
  fetchCountWeapon,
  selectComplete,
  fetchCurrentAttacker,
  fetchCurrentDefender,
  fetchAttackInfo,
  fetchDefenseInfo,
  fetchDoubtInfo,
  fetchExecuteInfo,
  addDoubtPass,
} = gameSlice.actions;
export default gameSlice.reducer;
