import Player from './player';
import Grid from '@mui/material/Grid';

function PlayerWithWeapon({ isMe, nickName }) {
  return (
    <Grid container>
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}></div>
      <Player isMe={isMe} nickName={ nickName } />
      <div style={{ width: 50, height: 50, backgroundColor: "grey" }}></div>
    </Grid>
  )
}

export default PlayerWithWeapon