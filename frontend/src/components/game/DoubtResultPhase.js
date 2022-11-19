import { useSelector } from "react-redux";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function DoubtResultPhase() {
  const me = useSelector((state) => state.game.me);
  const doubtResponse = useSelector((state) => state.game.doubtInfo).doubtResponse;

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
        <Box sx={{ position: "absolute", top: "4vmin", fontSize: "3.5vmin" }}>
          {doubtResponse.doubtSuccess ? "의심 성공" : "의심 실패"}
        </Box>
        <Box sx={{ fontSize: "2.5vmin", padding: "1vmin" }}>
          {doubtResponse.suspect.nickname}의 의심이
          {doubtResponse.doubtSuccess ? " 성공" : " 실패"}했습니다
        </Box>
        <Box sx={{ fontSize: "2.5vmin", padding: "1vmin" }}>
          {doubtResponse.doubtSuccess
            ? doubtResponse.suspected.nickname
            : doubtResponse.suspect.nickname}
          이 사망했습니다
        </Box>
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
        height: "97vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <BoxRender></BoxRender>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
