import React from "react";
import { useDispatch, useSelector } from "react-redux";

import { Card, Grid, Pagination } from "@mui/material";
import GameDescModal from "./GameDescModal";
import { gameDesc, gameListAll } from "../../_slice/tempGameSlice";

export default function GameList() {
  const [isSetting, setIsSetting] = React.useState(false);
  const dispatch = useDispatch();
  React.useEffect(() => {
    if (!isSetting) {
      setIsSetting(true);
      dispatch(gameListAll());
    }
  }, []);
  //temp data
  // const gameList = useSelector(state => state.tempGame.value.gameList)
  
  //real data
  const gameList = useSelector(state => state.tempGame.gameListAll)
  console.log("gmaelistall", gameList);


  // 방설정 모달
  const [open, setOpen] = React.useState(false);
  const roomSettingOpen = () => setOpen(true);
  const roomSettingClose = () => setOpen(false);

  // gameId props
  const [id, setId] = React.useState(0);
  const fetchGameInfo = (gameId, e) => {
    e.preventDefault();
    console.log("gameId", gameId);
    setId(gameId);
    //gameId로 게임 상세정보 불러오기
    dispatch(gameDesc(gameId));
  }
  //저장되어있는 게임 상세정보 store에서 불러오기
  // const bla =
  // {
  //   gameId: 1,
  //   title: '블라'
  // }

  // function gameListRender(gameList) {
  //   let arr = []
  //   console.log("ssss", gameList);
  //   for (let index = 0; index < gameList.length; index++) {
  //     arr.push(
  //       <Grid item key={index}>
  //           <Card sx={{ width: '23vw', height: '14vw' }} onClick={() => { roomSettingOpen(); fetchGameInfo(gameList[index].gameId); }}>
  //             {gameList[index].title}
  //           </Card>
  //       </Grid>
  //     )
  //   }
  //   return arr
  // }
  
  //pagination
  const [page, setPage] = React.useState(0)
  const [rowsPerPage, setRowsPerPage] = React.useState(6)

  const handleChangePage = (event, newPage) => {
    console.log(newPage);
    setPage(newPage)
  }

  return (
    <div>
      <Grid container spacing={3} columnSpacing={5} sx={{ pt: 5, justifyContent: 'center' }}>
        {gameList.games && gameList.games.map((game, key) => {
          return (
            <Grid item key={key}>
              <Card sx={{ width: '23vw', height: '14vw' }} onClick={(e) => { roomSettingOpen(); fetchGameInfo(game.gameId, e); }}>
                {game.title}
              </Card>
            </Grid>
          )
        }
        )}
        {/* {
        gameListRender(gameList)
        } */}
        <GameDescModal id={id} open={open} onClose={roomSettingClose}></GameDescModal>
      </Grid>
      <Pagination
        page={page}
        rowsPerPage={rowsPerPage}
        onChange={handleChangePage}
      >
      </Pagination>
    </div>
  )
}