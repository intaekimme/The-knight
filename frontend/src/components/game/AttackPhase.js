import { useSelector, useDispatch } from "react-redux";
import { selectWeaponForAttack } from "../../_slice/gameSlice";
import api from "../../api/api";
import PlayerWithWeaponList from "./PlayerWithWeaponList";
import { Box, Button, Paper } from "@mui/material";
import swordIconImg from "../../_assets/game/sword-icon.png";
import twinIconImg from "../../_assets/game/twin-icon.png";

export default function AttackPhase() {
  const me = useSelector((state) => state.game.me);
  const timer = useSelector((state) => state.game.timer).timer;
  const selectAttack = useSelector((state) => state.game.selectAttack);
  const currentAttacker = useSelector((state) => state.game.currentAttacker);
  const dispatch = useDispatch();

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const onPubAttackPass = () => {
    stompClient.send(api.pubAttackPass(gameId), {}, {});
  };

  const onSelectWeapon = (weapon, hand) => {
    dispatch(
      selectWeaponForAttack({
        weapon: weapon,
        hand: hand,
      })
    );
  };

  const onPass = () => {
    onPubAttackPass();
  };

  function BoxRender() {
    // 내가 공격자일 때
    if (me.memberId === currentAttacker.memberId) {
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
          <Box sx={{ textAlign: "center", fontSize: "3.5vmin" }}>공격 선택</Box>
          <Box
            sx={{
              display: "flex",
              alignItems: "flex-end",
              justifyContent: "space-evenly",
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
                sx={{
                  width: "23vmin",
                  display: "flex",
                  justifyContent: "space-between",
                }}
              >
                <Button
                  onClick={() => onSelectWeapon("SWORD", "LEFT")}
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
                    ...(selectAttack.weapon === "SWORD" &&
                      selectAttack.hand === "LEFT" && {
                        border: ".65vmin solid #e45826",
                      }),
                  }}
                >
                  <img
                    src={swordIconImg}
                    alt="SWORD"
                    style={{ width: "8vmin", height: "8vmin" }}
                  />
                </Button>
                <Button
                  onClick={() => onSelectWeapon("TWIN", "LEFT")}
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
                    ...(selectAttack.weapon === "TWIN" &&
                      selectAttack.hand === "LEFT" && {
                        border: ".65vmin solid #e45826",
                      }),
                  }}
                >
                  <img
                    src={twinIconImg}
                    alt="TWIN"
                    style={{ width: "8vmin", height: "8vmin" }}
                  />
                </Button>
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
                sx={{
                  width: "23vmin",
                  display: "flex",
                  justifyContent: "space-between",
                }}
              >
                <Button
                  onClick={() => onSelectWeapon("SWORD", "RIGHT")}
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
                    ...(selectAttack.weapon === "SWORD" &&
                      selectAttack.hand === "RIGHT" && {
                        border: ".65vmin solid #e45826",
                      }),
                  }}
                >
                  <img
                    src={swordIconImg}
                    alt="SWORD"
                    style={{ width: "8vmin", height: "8vmin" }}
                  />
                </Button>
                <Button
                  onClick={() => onSelectWeapon("TWIN", "RIGHT")}
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
                    ...(selectAttack.weapon === "TWIN" &&
                      selectAttack.hand === "RIGHT" && {
                        border: ".65vmin solid #e45826",
                      }),
                  }}
                >
                  <img
                    src={twinIconImg}
                    alt="TWIN"
                    style={{ width: "8vmin", height: "8vmin" }}
                  />
                </Button>
              </Box>
            </Box>
            <Button
              onClick={() => onPass()}
              color="dark"
              style={{
                width: "11.3vmin",
                height: "11.3vmin",
                backgroundColor: "#f0f0f0",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                fontSize: "3vmin",
                border: ".65vmin solid #424242",
                borderRadius: "1.3vmin",
              }}
            >
              PASS
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
            제한시간 : {timer}
          </Box>
        </Paper>
      );
      // 우리 팀이 공격자일 때
    } else if (me.team === currentAttacker.team) {
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
          <Box sx={{ fontSize: "2.5vmin" }}>아군이 공격을 선택 중입니다</Box>
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
            제한시간 : {timer}
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
          <Box sx={{ fontSize: "2.5vmin" }}>적팀이 공격을 선택 중입니다</Box>
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
