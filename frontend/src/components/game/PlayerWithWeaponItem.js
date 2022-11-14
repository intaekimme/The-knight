import Player from "./Player";
import { useDispatch, useSelector } from "react-redux";
import { deleteWeapon } from "../../_slice/gameSlice";
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

  function deleteLeft() {
    dispatch(deleteWeapon("left"));
  }
  function deleteRight() {
    dispatch(deleteWeapon("right"));
  }

  return (
    <div>
      <Box sx={{ display: "flex", justifyContent: "center" }}>
        <Box sx={{ display: "flex", flexDirection: "column" }}>
          <Box sx={{ display: "flex" }}>
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                transform: "translate(20%)",
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
                    onClick={deleteLeft}
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
              <div>L</div>
            </Box>
            <Player
              player={player}
              isOpp={isOpp}
            />
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                transform: "translate(-20%)",
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
                    onClick={deleteRight}
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
              <div>R</div>
            </Box>
          </Box>
          {phase !== "PREPARE" ? (
            <div
              style={{
                width: 30,
                height: 30,
                border: "solid",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              {player.order}
            </div>
          ) : null}
        </Box>
      </Box>
    </div>
  );
}

export default PlayerWithWeaponItem;
