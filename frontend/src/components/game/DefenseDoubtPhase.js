import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { addDoubtPass, initializePass } from "../../_slice/gameSlice";
import api from "../../api/api";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";

export default function DefenseDoubtPhase() {
  const dispatch = useDispatch();
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const players = useSelector((state) => state.game.players);
  const defenseInfo = useSelector((state) => state.game.defenseInfo);
  const isDead = players.players.find((player) => (player.memberId === me.memberId)).isDead; 

  const side = {
    LEFT: "왼쪽",
    RIGHT: "오른쪽",
  };

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const onPubDoubt = () => {
    // {
    //   suspected : {
    //     memberId : long : 의심 당하는 사람
    //   },
    //   doubtStatus: String
    //    (ATTACK_DOUBT, DEFENSE_DOUBT)
    // }
    const data = {
      suspected: {
        memberId: defenseInfo.defender.memberId,
      },
      doubtStatus: "DEFENSE_DOUBT",
    };
    stompClient.send(api.pubDoubt(gameId), {}, JSON.stringify(data));
    console.log(data);
  };

  const onPubDoubtPass = () => {
    stompClient.send(api.pubDoubtPass(gameId), {}, {});
    console.log("패스");
  };

  function clickDoubt() {
    onPubDoubt();
  }

  function clickPass() {
    onPubDoubtPass();
  }

  useEffect(() => {
    dispatch(initializePass());
  }, []);

  function BoxRender() {
    // 상대의 의심을 기다릴 때
    if (me.team === defenseInfo.defender.team) {
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
          <Box sx={{ fontSize: "2.5vmin" }}>적팀이 의심여부를 선택 중입니다</Box>
          <Box sx={{ position: "absolute", bottom: "2vmin", fontSize: "2vmin" }}>
            제한시간 : {timer}
          </Box>
        </Box>
      );
      // 우리 팀의 의심이지만, 나는 죽었을 때
    } else if (isDead) {
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
          <Box sx={{ fontSize: "2.5vmin" }}>아군이 의심여부를 선택 중입니다</Box>
          <Box sx={{ position: "absolute", bottom: "2vmin", fontSize: "2vmin" }}>
            제한시간 : {timer}
          </Box>
        </Box>
      );
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
            {defenseInfo.defender.nickname}이(가) {side[defenseInfo.hand]} 방패로 방어했습니다.
          </Box>
          <Box sx={{ display: "flex", alignItems: "flex-end", justifyContent: "space-evenly" }}>
            <Box
              onClick={() => clickDoubt()}
              sx={{
                width: "10vmin",
                height: "10vmin",
                backgroundColor: "#f0f0f0",
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
                fontSize: "3.5vmin",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              의심
            </Box>
            <Box
              onClick={() => clickPass()}
              sx={{
                width: "10vmin",
                height: "10vmin",
                backgroundColor: "#f0f0f0",
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
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
              position: "absolute",
              bottom: "2vmin",
              fontSize: "2vmin",
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
        height: "97vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <BoxRender></BoxRender>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
