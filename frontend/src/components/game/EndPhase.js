import { useSelector, useDispatch } from "react-redux"
import { useNavigate } from "react-router-dom"
import { resetGameSlice } from "../../_slice/gameSlice";
import api from "../../api/api";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import {Box, Button, Paper} from "@mui/material";

export default function EndPhase() {
  const me = useSelector((state) => state.game.me)
  const endInfo = useSelector((state) => state.game.endInfo)
  const navigate = useNavigate();
  const dispatch = useDispatch();
  
  const stompClient = useSelector((state) => state.websocket.stompClient);
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const onClick = () => {
    stompClient.send(api.pubEnd(gameId), {}, {})
    stompClient.disconnect();
    dispatch(resetGameSlice())
    navigate('/lobby');
  }

  function BoxRender() {
    return (
      <Paper
        sx={{
          width: "70vmin",
          height: "40vmin",
          backgroundColor: "#d9d9d9",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          position: "relative",
        }}
      >
        <Box sx={{ position: "absolute", top: "4vmin", fontSize: "3.5vmin" }}>{me.team === endInfo.winningTeam?"게임 승리":"게임 패배"}</Box>
        <Box sx={{ fontSize: "2.5vmin", padding: "1vmin" }}>{endInfo.winningTeam === "A"? "B": "A"}팀의 리더가 사망했습니다</Box>
        <Box sx={{ fontSize: "2.5vmin", padding: "1vmin" }}>{me.team === endInfo.winningTeam?"10 포인트 상승":"5 포인트 하락"}</Box>
        <Button
          onClick = {() => onClick()}
          color = "dark"
          style={{
            width: "25vmin",
            height: "6vmin",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            color: "white",
            fontSize: "2vmin",
            backgroundColor: "#878886",
            position: "absolute",
            bottom: "2vmin"
          }}
        >
          나가기
        </Button>
      </Paper>
    );
  }

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
      <BoxRender></BoxRender>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
