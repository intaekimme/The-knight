import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setBGM } from "../../_slice/gameSlice"
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import { Box, Paper } from "@mui/material";
import gameBGM from "../../_assets/game/sound/bgm-game.mp3";

export default function PredecessorPhase() {
  const dispatch = useDispatch();
  const attackFirst = useSelector((state) => state.game.attackFirst)
  const me = useSelector((state) => state.game.me)

  const gameBGMAudio = new Audio(gameBGM)

  useEffect(() => {
    gameBGMAudio.loop = true;
    gameBGMAudio.volume = 0.15;
    gameBGMAudio.play();
    dispatch(setBGM(gameBGMAudio));
  }, [])

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-around",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <Paper
        sx={{
          width: "70vmin",
          height: "40vmin",
          backgroundColor: "#d9d9d9",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          fontSize: "2.5vmin"
        }}
      >
        {attackFirst === me.team ? "아군이 선공입니다" : "적팀이 선공입니다"}
      </Paper>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
