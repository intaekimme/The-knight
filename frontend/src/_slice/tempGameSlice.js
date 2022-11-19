import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../api/api";
import axios from 'axios';
import GoogleLogin from '../commons/login/GoogleLogin';

// const gameListSliceInit = {
//   gameList: [
//     {
//       gameId: 1,
//       title: '첫 번째 방이여 ~',
//       status: '대기중',
//       capacity: 10,
//       participant: 5,
//   },
//   {
//     gameId: 2,
//     title: '두 번째 방이여 ~',
//     status: '게임중',
//     capacity: 8,
//     participant: 8,
//   },
//   {
//     gameId: 3,
//     title: '세 번째 방이여 ~',
//     status: '대기중',
//     capacity: 10,
//     participant: 7,
//   },
//   {
//     gameId: 4,
//     title: '네 번째 방이여 ~',
//     status: '대기중',
//     capacity: 6,
//     participant: 2,
//   },
//   {
//     gameId: 5,
//     title: '다섯 번째 방이여 ~',
//     status: '게임중',
//     capacity: 2,
//     participant: 2,
//   },
//   {
//     gameId: 6,
//     title: '여섯 번째 방이여 ~',
//     status: '대기중',
//     capacity: 4,
//     participant: 1,
//   },
//   {
//     gameId: 7,
//     title: '일곱 번째 방이여 ~',
//     status: '대기중',
//     capacity: 4,
//     participant: 1,
//   },
//   {
//     gameId: 8,
//     title: '여덟 번째 방이여 ~',
//     status: '대기중',
//     capacity: 4,
//     participant: 1,
//   },
//   {
//     gameId: 9,
//     title: '여섯 번째 방이여 ~',
//     status: '대기중',
//     capacity: 4,
//     participant: 1,
//   },
//   {
//     gameId: 10,
//     title: '아홉 번째 방이여 ~',
//     status: '대기중',
//     capacity: 4,
//     participant: 1,
//   },
// ]
// }

const gameListAll = createAsyncThunk('gameList/gameListAll', async (page) => {
  // return axios({
  //   method: "get",
  //   url: api.getGameList()
  // })
  //   .then(response => response.data)
  //   .catch(err => { console.log(err); })
  try {
    const res = await axios.get(api.getGameList() + `?page=${page}`, {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("get game list", res.data);
    return res.data;
  } catch (err) {
    console.log(err);
    if (err.response.status === 401) {
      GoogleLogin();
    }
  }
})
const searchRoom = createAsyncThunk('gameList/searchRoom', async (keyword) => {
  console.log("검색 키워드", keyword);
  try {
    const res = await axios.get(api.getGameList() + `?keyword=${keyword}`, {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("get game list", res.data);
    return res.data;
  } catch (err) {
    console.log(err);
    if (err.response.status === 401) {
      GoogleLogin();
    }
  }
})

const gameDesc = createAsyncThunk('gameList/gameDesc', async (gameId) => {
  try {
    const res = await axios.get(api.gameRoomInfo(gameId), {
      headers: { Authorization: `Bearer ${window.localStorage.getItem("loginToken")}` }
    });
    console.log("get gmae info", res.data);
    return res.data;
  } catch (err) {
    console.log(err);
    if (err.response.status === 401) {
      GoogleLogin();
    }
    return err;
  }
})

const gameListInit = [{
  gameId: -1,
  title: ' ',
  status: ' ',
  maxMember: -1,
  currentUser: -1,
}]

const gameInfoInit = {
  gameId: -1,
  title: "",
  maxMember: -1,
  currentMembers: -1,
  sword: -1,
  twin: -1,
  shield: -1,
  hand: -1
}

export const gameListSlice = createSlice({
  name: "gameList",
  initialState: { gameListAll: gameListInit, gameInfo: gameInfoInit },
  reducers: {
    // 수정할때 사용
    // setGameList: (state) => {
    // },
  },
  extraReducers: {
    [gameListAll.fulfilled]: (state, action) => {
      console.log(action);
      state.gameListAll = action.payload;
    },
    [gameListAll.rejected]: state => {
      state.gameListAll = gameListInit;
    },
    [searchRoom.fulfilled]: (state, action) => {
      state.gameListAll = action.payload;
    },
    [searchRoom.rejected]: state => {
      state.gameListAll = gameListInit;
    },
    [gameDesc.fulfilled]: (state, action) => {
      state.gameInfo = action.payload;
    },
    [gameDesc.rejected]: state => {
      state.gameInfo = gameInfoInit;
    },
  },
});
export { gameListAll, searchRoom, gameDesc };
export const { setGameList } = gameListSlice.actions;
export default gameListSlice.reducer;