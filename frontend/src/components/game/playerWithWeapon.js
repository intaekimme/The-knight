import Player from "./Player";
import Grid from "@mui/material/Grid";

// 추후에 isMe 삭제
function PlayerWithWeapon({ isMe, userName }) {
  return (
    <Grid container>
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}></div>
      <Player
        isMe={isMe}
        userName={userName}
      />
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}></div>
    </Grid>
  );
}

export default PlayerWithWeapon;
