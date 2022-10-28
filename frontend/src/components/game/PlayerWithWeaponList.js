import { useSelector } from "react-redux";
import PlayerWithWeaponItem from "./PlayerWithWeaponItem";
import Grid from "@mui/material/Grid";

function PlayerWithWeaponList() {
  const players = useSelector(state => state.game.players)

  function placePlayers(players) {
    let arr = [];
    if (players.peopleNum === 5) {
      arr.push(
        <Grid item xs={1}></Grid>
      )
    }
    for (let i = 1; i < players.peopleNum + 1; i++) {
      // A를 추후에 로그인한 유저의 팀과 비교하도록 변경
      arr.push(
        <Grid item xs={(players.peopleNum === 5) ? 2 : (12 / players.peopleNum)} key={players.teamA[`player${i}`].memberId}>
          {/* 추후에 isMe 삭제 */}
          <PlayerWithWeaponItem isMe={true} userName={players.teamA[`player${i}`].nickname} weapons={players.teamA[`player${i}`].weapons} />
        </Grid>
      );
    }
    return arr;
  }

  return (
    <Grid container>
      {placePlayers(players)}
    </Grid>
  )
}

export default PlayerWithWeaponList;