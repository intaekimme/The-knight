import { useSelector } from "react-redux";
import PlayerWithWeaponItem from "./PlayerWithWeaponItem";
import Box from "@mui/material/Box";

function PlayerWithWeaponList({ isOpp }) {
  const me = useSelector((state) => state.game.me);
  const players = useSelector((state) => state.game.players);

  return (
    <Box sx={{ width: "100%", display: "flex", justifyContent: "space-evenly"}}>
      {players.players
        .filter((player) => (isOpp ? player.team !== me.team : player.team === me.team))
        .map((player, index) => {
          return (
            <Box
              key={player.memberId}
            >
              <PlayerWithWeaponItem
                player={player}
                isOpp={isOpp}
              />
            </Box>
          );
        })}
    </Box>
  );
}

export default PlayerWithWeaponList;
