import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function ExecutePhase() {
  return (
    <div>
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <Box sx={{ width: 500, height: 300, backgroundColor: "grey" }}>
        우리 팀이 선공입니다.
      </Box>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </div>
  );
}
