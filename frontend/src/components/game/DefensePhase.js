import { useEffect } from "react";
import { useSelector } from "react-redux";
import api from "../../api/api"
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import {Box, Button, Paper} from "@mui/material";
import shieldIconImg from "../../_assets/game/image/shield-icon.png";
import clickSound from "../../_assets/game/sound/sound-click.mp3"
import ticktockSound from "../../_assets/game/sound/sound-ticktock.mp3";

export default function DefensePhase() {
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const attackInfo = useSelector((state) => state.game.attackInfo);
  const currentDefender = useSelector((state) => state.game.currentDefender);

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const clickAudio = new Audio(clickSound);
  const ticktockAudio = new Audio(ticktockSound);

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

  const onPubDefense = (payload) => {
    // {
    //   hand : String (LEFT, RIGHT)
    // }
    const data = {
      hand: payload
    }
    stompClient.send(api.pubDefense(gameId), {}, JSON.stringify(data));
  }

  const onPubDefensePass = () => {
    stompClient.send(api.pubDefensePass(gameId), {}, {});
  }

  function selectShield(hand) {
    clickAudio.play();
    onPubDefense(hand)
  }

  function selectPass() {
    clickAudio.play();
    onPubDefensePass()
  }

  useEffect(() => {
    if (timer <= 5) {
      ticktockAudio.play();
    }
  }, [timer]);

  function BoxRender() {
    // 내가 수비자일 때
    if (me.memberId === currentDefender.memberId) {
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
            {attackInfo.attacker.nickname}의 {side[attackInfo.hand]}{" "}
            {weaponsKr[attackInfo.weapon]}(으)로 공격 받았습니다
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
                width: "24.5vmin",
                display: "flex",
                justifyContent: "space-between",
              }}
            >
              <Box
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  borderRadius: "1.3vmin",
                  backgroundColor: "#424242",
                }}
              >
                <Box
                  sx={{
                    fontSize: "2vmin",
                    color: "#f0f0f0",
                    height: "3vmin",
                    lineHeight: "3vmin",
                  }}
                >
                  L
                </Box>
                <Button
                  onClick={() => selectShield("LEFT")}
                  color="dark"
                  style={{
                    width: "11.3vmin",
                    height: "11.3vmin",
                    backgroundColor: "#f0f0f0",
                    border: ".65vmin solid #424242",
                    borderRadius: "1.3vmin",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <img src={shieldIconImg} alt="SHIELD" style={{ width: "8vmin", height: "8vmin" }} />
                </Button>
              </Box>
              <Box
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  borderRadius: "1.3vmin",
                  backgroundColor: "#424242",
                }}
              >
                <Box
                  sx={{
                    fontSize: "2vmin",
                    color: "#f0f0f0",
                    height: "3vmin",
                    lineHeight: "3vmin",
                  }}
                >
                  R
                </Box>
                <Button
                  onClick={() => selectShield("RIGHT")}
                  color="dark"
                  style={{
                    width: "11.3vmin",
                    height: "11.3vmin",
                    backgroundColor: "#f0f0f0",
                    border: ".65vmin solid #424242",
                    borderRadius: "1.3vmin",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <img src={shieldIconImg} alt="SHIELD" style={{ width: "8vmin", height: "8vmin" }} />
                </Button>
              </Box>
            </Box>
            <Button
              onClick={() => selectPass()}
              color="dark"
              style={{
                width: "11.3vmin",
                height: "11.3vmin",
                backgroundColor: "#f0f0f0",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
                fontSize: "3vmin",
              }}
            >
              Pass
            </Button>
          </Box>
          <Box
            sx={{
              textAlign: "center",
              fontSize: "2vmin",
              position: "absolute",
              left: "50%",
              bottom: "2vmin",
              transform: "translate(-50%)",
            }}
          >
            제한시간: {timer}
          </Box>
        </Paper>
      );
      // 우리 팀이 공격자일 때
    } else if (me.team === currentDefender.team) {
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
          <Box sx={{ fontSize: "2.5vmin" }}>아군이 방어를 선택 중입니다</Box>
          <Box
            sx={{
              textAlign: "center",
              fontSize: "2vmin",
              position: "absolute",
              left: "50%",
              bottom: "2vmin",
              transform: "translate(-50%)",
            }}
          >
            제한시간: {timer}
          </Box>
        </Paper>
      );
      // 적팀이 공격자일 때
    } else {
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
          <Box sx={{ fontSize: "2.5vmin" }}>적팀이 방어를 선택 중입니다</Box>
          <Box
            sx={{
              textAlign: "center",
              fontSize: "2vmin",
              position: "absolute",
              left: "50%",
              bottom: "2vmin",
              transform: "translate(-50%)",
            }}
          >
            제한시간: {timer}
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
