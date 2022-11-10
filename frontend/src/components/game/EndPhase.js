import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function EndPhase() {
  function BoxRender() {
    return (
      <Box
      sx={{
        width: "70vmin",
        height: "40vmin",
        backgroundColor: "grey",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
      > 
        <Box>게임 승리 or 게임 패배</Box>
        <Box sx={{ display: "flex", alignItems: 'flex-end', justifyContent: "space-evenly" }}></Box>
        <Box sx={{ textAlign: "center" }}>
          <p>_팀의 리더가 사망했습니다</p>
          <p>10 포인트 하락</p>
        </Box>
        <Box 
          sx={{
            width: "25vmin",
            height: "8vmin",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            color: "white",
            backgroundColor: "#878886",
          }}
        >
          나가기
        </Box>
      </Box>
    )
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
