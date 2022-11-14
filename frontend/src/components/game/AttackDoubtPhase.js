import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { selectPass, initializePass } from "../../_slice/gameSlice";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function AttackDoubtPhase() {
  const dispatch = useDispatch();
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const attackInfo = useSelector((state) => state.game.attackInfo);
  const currentAttacker = useSelector((state) => state.game.currentAttacker);
  const weaponsKr = {
    SWORD: "검",
    TWIN: "쌍검",
    SHIELD: "방패",
    HAND: "맨손",
  };
  const side = {
    LEFT: "왼쪽",
    RIGHT: "오른쪽",
  };

  useEffect(() => {
    dispatch(initializePass());
  }, []);

  function clickPass() {
    "패스 클릭!";
  }

  function BoxRender() {
    // 공격자가 우리 팀일 때
    if (me.team === currentAttacker.team) {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "#d9d9d9",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            position: "relative",
          }}
        >
          <Box sx={{ fontSize: "2.5vmin" }}>
            적팀이 의심여부를 선택 중입니다
          </Box>
          <Box sx={{ position: "absolute", bottom: "2vmin" }}>
            제한시간 : {timer}
          </Box>
        </Box>
      );
      // 공격자가 적팀일 때
    } else {
      return (
        <Box
          sx={{
            width: "70vmin",
            height: "40vmin",
            backgroundColor: "#d9d9d9",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-evenly",
            position: "relative",
          }}
        >
          <Box sx={{ textAlign: "center", fontSize: "2.7vmin" }}>
            {attackInfo.attacker.nickname}이(가) {attackInfo.defender.nickname}
            을(를) {side[attackInfo.hand]} {weaponsKr[attackInfo.weapon]}(으)로
            공격했습니다.
          </Box>
          <Box
            sx={{
              display: "flex",
              alignItems: "flex-end",
              justifyContent: "space-evenly",
            }}
          >
            <Box
              sx={{
                width: "10vmin",
                height: "10vmin",
                backgroundColor: "#f0f0f0",
                border: "7px solid #4d4d4d",
                borderRadius: "10px",
                fontSize: "3.5vmin",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              의심
            </Box>
            <Box
              onClick={clickPass}
              sx={{
                width: "10vmin",
                height: "10vmin",
                backgroundColor: "#f0f0f0",
                border: "7px solid #4d4d4d",
                borderRadius: "10px",
                fontSize: "3.5vmin",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              Pass
            </Box>
          </Box>
          <Box
            sx={{
              fontSize: "2vmin",
              position: "absolute",
              bottom: "2vmin",
              left: "50%",
              transform: "translate(-50%)",
            }}
          >
            제한시간 : {timer}
          </Box>
        </Box>
      );
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
