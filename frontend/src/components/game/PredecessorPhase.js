import PlayerWithWeaponList from "./PlayerWithWeaponList";
import { Box } from "@mui/material";

export default function PredecessorPhase() {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        alignItems: "center",
        height: "90vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <Box
        sx={{
          width: 500,
          height: 300,
          backgroundColor: "grey",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        우리 팀이 선공입니다. or 상대팀이 선공입니다.
      </Box>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
