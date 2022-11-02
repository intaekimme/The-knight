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
  
  return (
    <div>
    <Grid container spacing={3} columnSpacing={5} sx={{pt: 3, justifyContent:'center'}}>
      {gameList.map((game, key) => {
        return(
          <Grid item key={key}>
            <Card sx={{ width: '23vw', height: '14vw' }} onClick={roomSettingOpen}>
              {game.title}
            </Card>
          </Grid>
          )
        }
        )}
        <GameDescModal open={open} onClose={ roomSettingClose }></GameDescModal>
    </Grid>
    <Pagination></Pagination>
    </div>
  )
}