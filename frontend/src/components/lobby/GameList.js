import React from "react";
import { useSelector } from "react-redux";

import { Card, Grid, Pagination } from "@mui/material";
import GameDescModal from "./GameDescModal";

export default function GameList() {
  const gameList = useSelector(state => state.tempGame.value.gameList)
  console.log("gamelist", gameList);

  // 방설정 모달
	const [open, setOpen] = React.useState(false);
	const roomSettingOpen = () => setOpen(true);
  const roomSettingClose = () => setOpen(false);
  
  const fetchGameInfo = (gameId) => {
    console.log("여기요 ~", gameId);
    //gameId로 게임 상세정보 불러오기
  }
  //저장되어있는 게임 상세정보 store에서 불러오기
  const bla = 
    {
      gameId: 1,
      title: '블라'
    }
  
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
  
  return (
    <div>
    <Grid container spacing={3} columnSpacing={5} sx={{pt: 3, justifyContent:'center'}}>
      {gameList.map((game, key) => {
        return(
          <Grid item key={key}>
            <Card sx={{ width: '23vw', height: '14vw' }} onClick={() => { roomSettingOpen(); fetchGameInfo(game.gameId); }}>
              {game.title}
            </Card>
          </Grid>
          )
        }
        )}
        {/* {
        gameListRender(gameList)
        } */}
        <GameDescModal bla={bla} open={open} onClose={ roomSettingClose }></GameDescModal>
    </Grid>
    <Pagination></Pagination>
    </div>
  )
}