import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";
import Sword from "./animation/Sword";

export default function ExecutePhase() {

  return (
    <div>
      <Sword startX={500} startY={500} endX={0} endY={0} isOpp={true}></Sword>
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
        <PlayerWithWeaponList></PlayerWithWeaponList>
      </Box>
    </div>
  );
}
