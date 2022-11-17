import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";
import Sword from "./animation/Sword";

export default function ExecutePhase() {
  const playersDOM = useSelector((state) => state.game.playersDOM);
  const executeInfo = useSelector((state) => state.game.executeInfo);

  return (
    <div sx={{ position: "relative" }}>
      {playersDOM[executeInfo.attacker.memberId.toString()] && (
        <Sword
          from={executeInfo.attacker.memberId}
          to={executeInfo.defender.memberId}
        ></Sword>
      )}
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
        <PlayerWithWeaponList></PlayerWithWeaponList>
      </Box>
    </div>
  );
}
