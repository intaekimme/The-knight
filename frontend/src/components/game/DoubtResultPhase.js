import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function DoubtResultPhase() {
  const me = useSelector((state) => state.game.me)
  const doubtInfo = useSelector((state) => state.game.doubtInfo)
  let isSuccess = null;
  if (me.team === doubtInfo.doubtTeam) {
    isSuccess = doubtInfo.doubtResult;
  } else {
    isSuccess = !doubtInfo.doubtResult;
  }

  function BoxRender() {
    return (
      <Box
        sx={{
          width: "70vmin",
          height: "40vmin",
          backgroundColor: "#d9d9d9",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          position: "relative",
        }}
      >
        <Box sx={{ position: "absolute", top: "2vmin" }}>
          {isSuccess ? "의심 성공" : "의심 실패"}
        </Box>
        <Box>_님의 의심이 성공했습니다</Box>
        <Box>_님이 사망했습니다</Box>
      </Box>
    );
  }

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
      <BoxRender></BoxRender>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
