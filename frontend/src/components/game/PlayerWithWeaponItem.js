import Player from "./Player";
import { useDispatch, useSelector } from "react-redux";
import api from "../../api/api"
import { Grid, Box } from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';

function PlayerWithWeaponItem({ player, isOpp }) {
  const phase = useSelector((state) => state.game.phase);
  const players = useSelector((state) => state.game.players);
  const isSelectComplete = useSelector((state) => state.game.isSelectComplete);
  const dispatch = useDispatch();
  const size = {
    2: "7vmin",
    3: "7vmin",
    4: "6vmin",
    5: "5vmin",
  }

  // const stompClient = useSelector((state) => state.websocket.stompClient);
  // const memberId = parseInt(window.localStorage.getItem("memberId"));
  // const myTeam = useSelector((state) => state.room.usersInfo).find(user => user.id === memberId).team;
  // const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const onPubDeleteWeapon = (payload) => {
    // {
    //   deleteHand: String
    //     (LEFT, RIGHT)
    // }
    const data = {
      deleteHand: payload
    }
    // stompClient.send(api.pubDeleteWeapon, {}, data);
    console.log(data)
  }

  function deleteHand(hand) {
    onPubDeleteWeapon(hand)
  }

  return (
    <div>
      <Box sx={{ display: "flex", justifyContent: "center" }}>
        <Box>
          <Box sx={{ display: "flex" }}>
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                transform: "translate(20%)",
                zIndex: 1,
              }}
            >
              <div
                style={{
                  width: size[(players.maxMember / 2)],
                  height: size[(players.maxMember / 2)],
                  ...(isSelectComplete
                    ? { backgroundColor: "#646464" }
                    : { backgroundColor: "#f0f0f0" }),
                  border: "7px solid #4d4d4d",
                  borderRadius: "10px",
                  position: "relative",
                }}
              >
                {player.weapons[0] && !isSelectComplete && (
                  <CloseIcon
                    onClick={() => deleteHand("LEFT")}
                    sx={{
                      ...(isOpp && { display: "none" }),
                      position: "absolute",
                      top: 0,
                      left: 0,
                      transform: "translate(-70%, -70%)",
                      backgroundColor: "#d9d9d9",
                      border: "3px solid #33363f",
                      borderRadius: "50%",
                      fontSize: "small",
                    }}
                  ></CloseIcon>
                )}
                <span style={isOpp && { display: "none" }}>
                  {player.weapons[0]}
                </span>
              </div>
              <Box sx={{ fontSize: "2vmin" }}>L</Box>
            </Box>
            <Box sx={{ position: "relative"}}>
              <Player
                player={player}
                isOpp={isOpp}
              />
              {phase !== "PREPARE" ? (
                <div
                  style={{
                    width: "3vmin",
                    height: "3vmin",
                    border: "3px solid black",
                    backgroundColor: "white",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    position: "absolute",
                    bottom: "2.5vmin",
                    left: ".5vmin",
                    fontSize: "2vmin",
                  }}
                >
                  {player.order}
                </div>
              ) : null}
            </Box>
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                transform: "translate(-20%)",
                zIndex: 1,
              }}
            >
              <div
                style={{
                  width: size[(players.maxMember / 2)],
                  height: size[(players.maxMember / 2)],
                  ...(isSelectComplete
                    ? { backgroundColor: "#646464" }
                    : { backgroundColor: "#f0f0f0" }),
                  border: "7px solid #4d4d4d",
                  borderRadius: "10px",
                  position: "relative",
                }}
              >
                {player.weapons[1] && !isSelectComplete && (
                  <CloseIcon
                    onClick={() => deleteHand("RIGHT")}
                    sx={{
                      ...(isOpp && { display: "none" }),
                      position: "absolute",
                      top: 0,
                      left: 0,
                      transform: "translate(-70%, -70%)",
                      backgroundColor: "#d9d9d9",
                      border: "3px solid #33363f",
                      borderRadius: "50%",
                      fontSize: "small",
                    }}
                  ></CloseIcon>
                )}
                <span style={isOpp && { display: "none" }}>
                  {player.weapons[1]}
                </span>
              </div>
              <Box sx={{ fontSize: "2vmin" }}>R</Box>
            </Box>
          </Box>
        </Box>
      </Box>
    </div>
  );
}

export default PlayerWithWeaponItem;
