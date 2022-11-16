import { useSelector } from 'react-redux';
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import { Box } from "@mui/material";

export default function PredecessorPhase() {
  const attackFirst = useSelector((state) => state.game.attackFirst)
  const me = useSelector((state) => state.game.me)

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        alignItems: "center",
        height: "88vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <Box
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
      </Box>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
