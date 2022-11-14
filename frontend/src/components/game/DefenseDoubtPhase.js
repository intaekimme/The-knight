import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux"
import { selectPass, initializePass } from "../../_slice/gameSlice"
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function DefenseDoubtPhase() {
  const dispatch = useDispatch();
  const me = useSelector(state => state.game.me)
  const timer = useSelector((state) => state.game.timer).timer;
  const currentDefender = useSelector(state => state.game.currentDefender)

  useEffect(() => {
    dispatch(initializePass())
  }, []);

  function clickPass() {
    "패스 클릭!"
  }

  function BoxRender() {
    // 방어자가 우리 팀일 때
    if (me.team === currentDefender.team) {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "#d9d9d9",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            position: "relative"
          }}
        >
          <Box sx={{ fontSize: "2.5vmin" }}>적팀이 의심여부를 선택 중입니다</Box>
          <Box sx={{ position: "absolute", bottom: "2vmin", fontSize: "2vmin" }}>
            제한시간 : {timer}
          </Box>
        </Box>
      )
    // 방어자가 적팀일 때
    } else {
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
          <Box sx={{ textAlign: "center" }}>{currentDefender.nickname}이 _쪽 방패로 방어했습니다.</Box>
          <Box sx={{ display: "flex", alignItems: 'flex-end', justifyContent: "space-evenly" }}>
            <Box sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>의심</Box>
            <Box onClick={() => clickPass} sx={{ width: "11vmin", height: "11vmin", backgroundColor: "#e2e2e2"}}>Pass</Box>
          </Box>
          <Box sx={{ textAlign: "center"}}>제한시간: 100</Box>
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
