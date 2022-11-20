import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { addDoubtPass, initializePass } from "../../_slice/gameSlice";
import api from "../../api/api";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import { Box, Button, Paper } from "@mui/material";
import clickSound from "../../_assets/game/sound/sound-click.mp3"
import ticktockSound from "../../_assets/game/sound/sound-ticktock.mp3";

export default function AttackDoubtPhase() {
  const dispatch = useDispatch();
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const players = useSelector((state) => state.game.players);
  const attackInfo = useSelector((state) => state.game.attackInfo);
  const currentAttacker = useSelector((state) => state.game.currentAttacker);
  const isDead = players.players.find(
    (player) => player.memberId === me.memberId
  ).isDead;

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
  
  const clickAudio = new Audio(clickSound)
  const ticktockAudio = new Audio(ticktockSound);

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
        memberId: attackInfo.attacker.memberId,
      },
      doubtStatus: "ATTACK_DOUBT",
    };
    stompClient.send(api.pubDoubt(gameId), {}, JSON.stringify(data));
  };

  const onPubDoubtPass = () => {
    stompClient.send(api.pubDoubtPass(gameId), {}, {});
  };

  function clickDoubt() {
    clickAudio.play();
    onPubDoubt();
  }

  function clickPass() {
    clickAudio.play();
    onPubDoubtPass();
  }

  useEffect(() => {
    dispatch(initializePass());
  }, []);

  useEffect(() => {
    if (timer <= 5) {
      ticktockAudio.play();
    }
  }, [timer]);

  function BoxRender() {
    // 상대의 의심을 기다릴 때
    if (me.team === currentAttacker.team) {
      return (
        <Paper
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
          <Box
            sx={{ position: "absolute", bottom: "2vmin", fontSize: "2vmin" }}
          >
            제한시간 : {timer}
          </Box>
        </Paper>
      );
      // 우리 팀의 의심이지만, 나는 죽었을 때
    } else if (isDead) {
      return (
        <Paper
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
            아군이 의심여부를 선택 중입니다
          </Box>
          <Box
            sx={{ position: "absolute", bottom: "2vmin", fontSize: "2vmin" }}
          >
            제한시간 : {timer}
          </Box>
        </Paper>
      );
    } else {
      return (
        <Paper
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
            공격했습니다
          </Box>
          <Box
            sx={{
              display: "flex",
              alignItems: "flex-end",
              justifyContent: "space-evenly",
            }}
          >
            <Button
              onClick={() => clickDoubt()}
              color="dark"
              sx={{
                width: "11.3vmin",
                height: "11.3vmin",
                backgroundColor: "#f0f0f0",
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
                fontSize: "3vmin",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              의심
            </Button>
            <Button
              onClick={() => clickPass()}
              color="dark"
              sx={{
                width: "11.3vmin",
                height: "11.3vmin",
                backgroundColor: "#f0f0f0",
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
                fontSize: "3vmin",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              Pass
            </Button>
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
        </Paper>
      );
    }
  }

  return (
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
  );
}
