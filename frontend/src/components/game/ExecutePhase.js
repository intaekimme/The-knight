import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";
import Sword from "./animation/Sword";
import Shield from "./animation/Shield";

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
      {playersDOM[executeInfo.attacker.memberId.toString()] && (
        <Shield
          defender={executeInfo.defender.memberId}
        ></Shield>
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
