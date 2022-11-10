import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function DoubtResultPhase() {
  function BoxRender() {
    return (
      <Box
      sx={{
        width: "70vmin",
        height: "40vmin",
        backgroundColor: "grey",
        display: "flex",
        flexDirection: "column",
      }}
      > 
        <Box sx={{ textAlign: "center" }}>의심 성공 or 의심 실패</Box>
        <Box sx={{ display: "flex", alignItems: 'flex-end', justifyContent: "space-evenly" }}></Box>
        <Box sx={{ textAlign: "center" }}>
          <p>_님의 의심이 성공했습니다</p>
          <p>_님이 사망했습니다</p>
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
