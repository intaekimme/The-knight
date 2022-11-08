import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";
import { useSelector } from "react-redux"

export default function AttackPhase() {
  const me = useSelector(state => state.game.me)
  const currentAttacker = useSelector(state => state.game.currentAttacker)

  function BoxRender() {
    // 내가 공격자일 때
    if (me.memberId === currentAttacker.memberId) {
      return (
        <Box
        sx={{
          width: "70vmin",
          height: "40vmin",
          backgroundColor: "grey",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-evenly",
        }}
        > 
          <Box sx={{ textAlign: "center" }}>공격 선택</Box>
          <Box sx={{ display: "flex", alignItems: 'flex-end', justifyContent: "space-evenly" }}>
            <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center"}}>
              L
              <Box sx={{ width: "23vmin", display: "flex", justifyContent: "space-between" }}>
                <Box sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>sword</Box>
                <Box sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>twin sword</Box>
              </Box>
            </Box>
            <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
              R
              <Box sx={{ width: "23vmin", display: "flex", justifyContent: "space-between"}}>
                <Box sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>sword</Box>
                <Box sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>twin sword</Box>
              </Box>
            </Box>
            <Box sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>Pass</Box>
          </Box>
          <Box sx={{ textAlign: "center"}}>제한시간: 100</Box>
        </Box>
      )
    // 우리 팀이 공격자일 때 
    } else if (me.team === currentAttacker.team) {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "grey",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          아군이 공격을 선택 중입니다
        </Box>
      )
    // 적팀이 공격자일 때
    } else {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "grey",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          적팀이 공격을 선택 중입니다
        </Box>
      )
    }
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
