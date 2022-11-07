import { useSelector } from "react-redux";
import PlayerWithWeaponItem from "./PlayerWithWeaponItem";
import Grid from "@mui/material/Grid";

function PlayerWithWeaponList({ isOpp }) {
  const me = useSelector((state) => state.game.me);
  const players = useSelector((state) => state.game.players);
  const numberInTeam = players.maxUser / 2;

  return (
    <Grid container>
      {players.players
        .filter((player) => (isOpp ? player.team !== me.team : player.team === me.team))
        .map((player, index) => {
          return (
            <Grid
              item
              xs={numberInTeam === 5 ? 2 : 12 / numberInTeam}
              key={player.memberId}
            >
              <PlayerWithWeaponItem
                player={player}
                isOpp={isOpp}
              />
            </Grid>
          );
        })}
    </Grid>
  );
}

export default PlayerWithWeaponList;
