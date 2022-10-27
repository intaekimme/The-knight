import Player from "./Player";
import Grid from "@mui/material/Grid";

// 추후에 isMe 삭제
function PlayerWithWeapon({ isMe, userName, weapons }) {
  return (
    <Grid container>
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>{weapons[0]}</div>
      <Player
        isMe={isMe}
        userName={userName}
      />
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}>{weapons[1]}</div>
    </Grid>
  );
}

export default PlayerWithWeapon;
