import Player from "./Player";
import { useDispatch, useSelector } from "react-redux";
import { deleteWeapon } from "../../_slice/gameSlice";
import { Grid, Box } from "@mui/material";
import CancelIcon from "@mui/icons-material/Cancel";

// 추후에 isMe 삭제
function PlayerWithWeaponItem({ player, isOpp }) {
  const phase = useSelector((state) => state.game.phase);
  const dispatch = useDispatch();
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
          <Box sx={{ display: "flex"}}>
            <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
              <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>
                {player.weapons[0] && (
                  <CancelIcon
                    onClick={deleteLeft}
                    style={isOpp && { display: "none" }}
                  ></CancelIcon>
                )}
                <span style={isOpp && { display: "none" }}>{player.weapons[0]}</span>
              </div>
              <div>L</div>
            </Box>
            <Player player={player} />
            <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
              <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>
                {player.weapons[1] && (
                  <CancelIcon
                    onClick={deleteRight}
                    style={isOpp && { display: "none" }}
                  ></CancelIcon>
                )}
                <span style={isOpp && { display: "none" }}>{player.weapons[1]}</span>
              </div>
              <div>R</div>
            </Box>
          </Box>
          {/* 순서 표시하는 로직 필요 */}
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
