import React from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

// LOADING, PREPARE, PREDECESSOR, ATTACK, ATTACK_DOUBT, DEFENSE, DEFENSE_DOUBT, DOUBT_RESULT, EXECUTE(공&방 결과), END
const phaseInit = "LOADING";

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
      leftCount: 1,
      rightCount: 3,
      isDead: false,
      team: "A",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 2,
      nickname: "Smith",
      leftCount: 0,
      rightCount: 0,
      isDead: true,
      team: "A",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 3,
      nickname: "Sara",
      leftCount: 0,
      rightCount: 0,
      isDead: false,
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
      isDead: false,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 7,
      nickname: "FTX",
      leftCount: 0,
      rightCount: 0,
      isDead: false,
      team: "B",
      order: 0,
      weapons: [null, null],
    },
    {
      memberId: 8,
      nickname: "Sam",
      leftCount: 0,
      rightCount: 0,
      isDead: false,
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

const orderInit = [null];

const countWeaponInit = {
  sword: 0,
  twin: 0,
  shield: 0,
  hand: 0,
};

const isSelectCompleteInit = false;


const subscribeObjectInit = {
  prepare: [],
  predecessor: [],
  attack: [],
  attackDoubt: [],
  defense: [],
  defenseDoubt: [],
  doubtResult: [],
  execute: [],
  end: [],
}


const currentAttackerInit = {
  memberId: 6,
  team: "B",
};

const currentDefenderInit = {
  memberId: 3,
  team: "A",
};

const selectAttackInit = {
  weapon: null,
  hand: null,
}

const attackInfoInit = {
  attacker: {
    memberId: 0,
    nickname: "",
    team: "",
  },
  defender: {
    memberId: 0,
    nickname: "",
    team: ""
  },
  weapon: "",
  hand: "",
}

const defenseInfoInit = {
  defender: {
    memberId: 0,
    nickname: "",
    team: "",
  },
  weapon: "",
  hand: "",
}

const doubtInfoInit =   {
  doubtResponse : {
    suspect : {
      memberId : 0,
      nickname: "",
      isDead : false,
    },
    suspected : {
      memberId : 0,
      nickname: "",
      isDead : false,
      weapon: "",
      hand: "",
    },
    doubtTeam: "",  // 의심을 건 사람의 팀
    doubtSuccess: false,  // 의심 성공 여부
  },
  doubtStatus : "",
}

const doubtPassListInit = [1]

const executeInfoInit = {
  attackTeam: "",
  attacker: {
    memberId: 6,
    weapon: "",
    hand: "",
  },
  defender : {
    memberId: 3,
    hand: "",
    isDead: false,
    hitCount: 0,
    passedDefense: false,
  }
}

const endInfoInit = {
  winningTeam: "",
  teamALeaderId: 0,
  teamBLeaderId: 0,
  players: [
    {
      memberId: 0,
      nickname: "",
      team: "",
      leftCount: 0,
      rightCount: 0,
      order: 0,
      weapons: [null, null]
    }
  ],
};

const playersDOMInit = {}

const initialState = {
  me: meInit,
  timer: timerInit,
  players: playersInit,
  leader: leaderInit,
  attakFirst: attackFirstInit,
  order: orderInit,
  countWeapon: countWeaponInit,
  isSelectComplete: isSelectCompleteInit,
  phase: phaseInit,
  subscribeObject: subscribeObjectInit,
  currentAttacker: currentAttackerInit,
  currentDefender: currentDefenderInit,
  selectAttack: selectAttackInit,
  attackInfo: attackInfoInit,
  defenseInfo: defenseInfoInit,
  doubtInfo: doubtInfoInit,
  doubtPassList: doubtPassListInit,
  executeInfo: executeInfoInit,
  endInfo: endInfoInit,
  playersDOM: playersDOMInit,
}

export const gameSlice = createSlice({
  name: "gameSlice",
  initialState: initialState,
  reducers: {
    resetGameSlice: () => initialState,
    setMe: (state) => {
      const memberId = parseInt(window.localStorage.getItem("memberId"));
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
      if (state.timer.intervalObject) {
        clearInterval(state.timer.intervalObject);
      }
      state.timer.timer = action.payload;
    },
    countTimer: (state, action) => {
      const later = state.timer.timer - 1;
      state.timer = {
        timer: later,
        intervalObject: action.payload,
      }
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
    fetchOrder: (state, action) => {
      state.order = action.payload;
    },
    fetchPhase: (state, action) => {
      state.phase = action.payload;
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
      state.countWeapon = action.payload;
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
    },
    selectWeaponForAttack: (state, action) => { 
      state.selectAttack = {
        weapon: action.payload.weapon,
        hand: action.payload.hand,
      };
    },
    resetWeaponForAttack: (state) => {
      state.selectAttack = {
        weapon: null,
        hand: null,
      }
    },
    addSubscribeObject: (state, action) => { 
      if (action.payload.phase === "PREPARE") {
        state.subscribeObject.prepare.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "PREDECESSOR") {
        state.subscribeObject.predecessor.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "ATTACK") {
        state.subscribeObject.attack.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "ATTACK_DOUBT") {
        state.subscribeObject.attackDoubt.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "DEFENSE") {
        state.subscribeObject.defense.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "DEFENSE_DOUBT") {
        state.subscribeObject.defenseDoubt.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "DOUBT_RESULT") {
        state.subscribeObject.doubtResult.push(action.payload.subscribeObject)
      } else if (action.payload.phase === "END") {
        state.subscribeObject.end.push(action.payload.subscribeObject)
      }
    },
    cancelSubscribe: (state, action) => {
      if (action.payload === "PREPARE") {
        state.subscribeObject.prepare.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.prepare = []
      } else if (action.payload === "PREDECESSOR") {
        state.subscribeObject.predecessor.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.predecessor = []
      } else if (action.payload === "ATTACK") {
        state.subscribeObject.attack.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.attack = []
      } else if (action.payload === "ATTACK_DOUBT") {
        state.subscribeObject.attackDoubt.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.attackDoubt = []
      } else if (action.payload === "DEFENSE") {
        state.subscribeObject.defense.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.defense = []
      } else if (action.payload === "DEFENSE_DOUBT") {
        state.subscribeObject.defenseDoubt.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.defenseDoubt = []
      } else if (action.payload === "DOUBT_RESULT") {
        state.subscribeObject.doubtResult.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.doubtResult = []
      } else if (action.payload === "END") {
        state.subscribeObject.end.forEach(subscribeObject => subscribeObject.unsubscribe())
        state.subscribeObject.end = []
      }
    },
    setDOM: (state, action) => {
      state.playersDOM[action.payload.memberIdString] = action.payload.dom
    },
  },
});
export const {
  resetGameSlice,
  setMe,
  setTimer,
  countTimer,
  stopCountTimer,
  fetchPlayers,
  fetchOrder,
  fetchPhase,
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
  selectWeaponForAttack,
  resetWeaponForAttack,
  addSubscribeObject,
  cancelSubscribe,
  setDOM,
} = gameSlice.actions;
export default gameSlice.reducer;
