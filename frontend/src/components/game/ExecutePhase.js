import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import { Box, Paper } from "@mui/material";
import Sword from "./animation/Sword";
import SecondSword from "./animation/SecondSword";
import Shield from "./animation/Shield";

export default function ExecutePhase() {
  const playersDOM = useSelector((state) => state.game.playersDOM);
  const executeInfo = useSelector((state) => state.game.executeInfo);
  const isTwin = executeInfo.attacker.weapon === "TWIN";
  const isDefensePass = executeInfo.defender.passedDefense;
  const [isStart, setIsStart] = useState(false);

  function BoxRender() {
    return (
      <Paper
        sx={{ width: "70vmin", height: "40vmin", visibility: "hidden" }}
      ></Paper>
    );
  }

  useEffect(() => {
    setIsStart(true);
  })

  return (
    <div sx={{ position: "relative" }}>
      {isStart && playersDOM[executeInfo.attacker.memberId.toString()] && (
        <div>
          <Sword
            from={executeInfo.attacker.memberId}
            to={executeInfo.defender.memberId}
          ></Sword>
          <SecondSword
            from={executeInfo.attacker.memberId}
            to={executeInfo.defender.memberId}
          ></SecondSword>
          <Shield
            defender={executeInfo.defender.memberId}
            isTwin={isTwin}
          ></Shield>
        </div>
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
        <BoxRender></BoxRender>
        <PlayerWithWeaponList></PlayerWithWeaponList>
      </Box>
    </div>
  );
}
