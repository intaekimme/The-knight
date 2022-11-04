import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../api/api";
import axios from 'axios';

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

const gameListAll = createAsyncThunk('gameListAll', async () => {
  // return axios({
  //   method: "get",
  //   url: api.getGameList()
  // })
  //   .then(response => response.data)
  //   .catch(err => { console.log(err); })
  try {
    const res = await axios.get(api.getGameList(), {
      headers: {Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}
    });
    console.log("get game list", res.data);
    return res.data;
  } catch (err) {
    console.log(err);
  }
})
  
const gameListInit = [{
  gameId: -1,
  title: ' ',
  status: ' ',
  maxUser: -1,
  currentUser: -1,
}]

export const gameListSlice = createSlice({
  name: "gameListSlice",
  initialState: { gameListAll: gameListInit },
  reducers: {
    // 수정할때 사용
    // setGameList: (state) => {
    // },
  },
  extraReducers: {
    [gameListAll.fulfilled]: (state, action) => {
      state.gameListAll = action.payload;
    },
    [gameListAll.rejected]: state => {
      state.gameListAll = gameListInit;
    },
  },
});
export { gameListAll };
export const { setGameList } = gameListSlice.actions;
export default gameListSlice.reducer;