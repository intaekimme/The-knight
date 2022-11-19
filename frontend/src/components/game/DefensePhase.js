import { useSelector } from "react-redux";
import api from "../../api/api"
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import Box from "@mui/material/Box";
import shieldIconImg from "../../_assets/game/shield-icon.png";

export default function DefensePhase() {
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const attackInfo = useSelector((state) => state.game.attackInfo);
  const currentDefender = useSelector((state) => state.game.currentDefender);

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

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
    console.log(data)
  }

  const onPubDefensePass = () => {
    stompClient.send(api.pubDefensePass(gameId), {}, {});
    console.log("패스!")
  }

  function selectShield(hand) {
    onPubDefense(hand)
  }

  function selectPass() {
    onPubDefensePass()
  }

  function BoxRender() {
    // 내가 수비자일 때
    if (me.memberId === currentDefender.memberId) {
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
                <Box
                  onClick={() => selectShield("LEFT")}
                  sx={{
                    width: "10vmin",
                    height: "10vmin",
                    backgroundColor: "#f0f0f0",
                    border: ".65vmin solid #424242",
                    borderRadius: "1.3vmin",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <img src={shieldIconImg} alt="SHIELD" style={{ width: "8vmin", height: "8vmin" }} />
                </Box>
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
                <Box
                  onClick={() => selectShield("RIGHT")}
                  sx={{
                    width: "10vmin",
                    height: "10vmin",
                    backgroundColor: "#f0f0f0",
                    border: ".65vmin solid #424242",
                    borderRadius: "1.3vmin",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <img src={shieldIconImg} alt="SHIELD" style={{ width: "8vmin", height: "8vmin" }} />
                </Box>
              </Box>
            </Box>
            <Box
              onClick={() => selectPass()}
              sx={{
                width: "10vmin",
                height: "10vmin",
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
            </Box>
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
        </Box>
      );
      // 우리 팀이 공격자일 때
    } else if (me.team === currentDefender.team) {
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
        </Box>
      );
      // 적팀이 공격자일 때
    } else {
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
        height: "100vh",
      }}
    >
      <PlayerWithWeaponList isOpp={true}></PlayerWithWeaponList>
      <BoxRender></BoxRender>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </Box>
  );
}
