import { createSlice } from "@reduxjs/toolkit";

const gameListSliceInit = {
  gameList: [
    {
      gameId: 1,
      title: '첫 번째 방이여 ~',
      status: '대기중',
    capacity: 10,
    participant: 5,
  },
  {
    gameId: 2,
    title: '두 번째 방이여 ~',
    status: '게임중',
    capacity: 8,
    participant: 8,
  },
  {
    gameId: 3,
    title: '세 번째 방이여 ~',
    status: '대기중',
    capacity: 10,
    participant: 7,
  },
  {
    gameId: 4,
    title: '네 번째 방이여 ~',
    status: '대기중',
    capacity: 6,
    participant: 2,
  },
  {
    gameId: 5,
    title: '다섯 번째 방이여 ~',
    status: '게임중',
    capacity: 2,
    participant: 2,
  },
  {
    gameId: 6,
    title: '여섯 번째 방이여 ~',
    status: '대기중',
    capacity: 4,
    participant: 1,
  },
]
}

export const gameListSlice = createSlice({
  name: "gameListSlice",
  initialState: { value: gameListSliceInit },
  reducers: {
    changeImage: (state, action) => {
      state.value.MemberInfo.image = action.payload
    },
},
});
export const { changeImage } = gameListSlice.actions;
export default gameListSlice.reducer;