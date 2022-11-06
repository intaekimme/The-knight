import Player from "./Player";
import { useDispatch, useSelector } from "react-redux"
import { deleteWeapon } from "../../_slice/gameSlice"
import Grid from "@mui/material/Grid";
import CancelIcon from '@mui/icons-material/Cancel';

// 추후에 isMe 삭제
function PlayerWithWeaponItem({ player, isOpp }) {
  const phase = useSelector(state => state.game.phase)  
  const dispatch = useDispatch();
  function deleteLeft() {
    dispatch(deleteWeapon('left'))
  }
  function deleteRight() {
    dispatch(deleteWeapon('right'))
  }

  return (
    <div>
      <Grid container>
        <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>
          {player.weapons[0] && <CancelIcon onClick={deleteLeft} style={isOpp ? {display: "none"} : null}></CancelIcon>}
          <span style={isOpp ? {display: "none"} : null}>{player.weapons[0]}</span>
        </div>
        <Player
          player={ player }
        />
        <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>
          {player.weapons[1] && <CancelIcon onClick={deleteRight} style={isOpp ? {display: "none"} : null}></CancelIcon>}
          <span style={isOpp ? {display: "none"} : null}>{player.weapons[1]}</span>
        </div>
      </Grid>
      {/* 순서 표시하는 로직 필요 */}
      {(phase !== "PRE") ? <div style={{ width: 50, height: 50, border: "solid" }}>{player.order}</div> : null}
    </div>
  );
}

export default PlayerWithWeaponItem;
