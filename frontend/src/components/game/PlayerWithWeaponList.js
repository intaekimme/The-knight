import { useSelector } from "react-redux";
import PlayerWithWeaponItem from "./PlayerWithWeaponItem";
import Grid from "@mui/material/Grid";

function PlayerWithWeaponList({isOpp}) {
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
        <Grid item xs={(players.peopleNum === 5) ? 2 : (12 / players.peopleNum)} key= {isOpp ? players.teamB[`player${i}`].memberId : players.teamA[`player${i}`].memberId}>
          <PlayerWithWeaponItem player={isOpp ? players.teamB[`player${i}`] : players.teamA[`player${i}`]} isOpp={isOpp} />
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