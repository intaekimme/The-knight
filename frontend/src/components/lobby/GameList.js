import React from "react";

import { Card, Grid, Pagination } from "@mui/material";
import { useSelector } from "react-redux";

export default function GameList() {
  const gameList = useSelector(state => state.tempGame.value.gameList)
  console.log("gamelist", gameList);
  
  return (
    <Grid container spacing={5} sx={{pt: 5, justifyContent:'center'}}>
      {gameList.map((game, key) => {
        return(
          <Grid item key={key}>
            <Card sx={{ width: 345, height: 210 }}>
              {game.title}
            </Card>
          </Grid>
          )
        }
        )}
      <Pagination></Pagination>
    </Grid>
  )
}