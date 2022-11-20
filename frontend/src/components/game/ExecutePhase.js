import { useEffect } from "react";
import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";
import Sword from "./animation/Sword";
import SecondSword from "./animation/SecondSword";
import Shield from "./animation/Shield";

export default function ExecutePhase() {
  const playersDOM = useSelector((state) => state.game.playersDOM);
  const executeInfo = useSelector((state) => state.game.executeInfo);
  const isTwin = executeInfo.attacker.weapon === "TWIN"

  return (
    <div sx={{ position: "relative" }}>
      {playersDOM[executeInfo.attacker.memberId.toString()] && (
        <Sword
          from={executeInfo.attacker.memberId}
          to={executeInfo.defender.memberId}
        ></Sword>
      )}
      {playersDOM[executeInfo.attacker.memberId.toString()] && isTwin && (
        <SecondSword
          from={executeInfo.attacker.memberId}
          to={executeInfo.defender.memberId}
        ></SecondSword>
      )}
      {playersDOM[executeInfo.attacker.memberId.toString()] && (
        <Shield
          defender={executeInfo.defender.memberId}
          isTwin={isTwin}
        ></Shield>
      )}
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
        <PlayerWithWeaponList></PlayerWithWeaponList>
      </Box>
    </div>
  );
}
