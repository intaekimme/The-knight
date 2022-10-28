import Player from "./Player";
import { useDispatch } from "react-redux"
import { deleteWeapon } from "../../_slice/playersSlice"
import Grid from "@mui/material/Grid";
import CancelIcon from '@mui/icons-material/Cancel';

// 추후에 isMe 삭제
function PlayerWithWeaponItem({ isMe, userName, weapons }) {
  const dispatch = useDispatch();
  function deleteLeft() {
    dispatch(deleteWeapon('left'))
  }
  function deleteRight() {
    dispatch(deleteWeapon('right'))
  }

  return (
    <Grid container>
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>
        {weapons[0] && <CancelIcon onClick={deleteLeft}></CancelIcon>}
        {weapons[0]}
      </div>
      <Player
        isMe={isMe}
        userName={userName}
      />
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>
        {weapons[1] && <CancelIcon onClick={deleteRight}></CancelIcon>}
        {weapons[1]}
      </div>
    </Grid>
  );
}

export default PlayerWithWeaponItem;
