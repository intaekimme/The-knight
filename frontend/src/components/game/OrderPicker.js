import { useSelector, useDispatch } from "react-redux";
import { selectOrder } from "../../_slice/gameSlice"
import Player from "./Player"
import Grid from "@mui/material/Grid";

function OrderPicker() {
  const dispatch = useDispatch();
  const me = useSelector(state => state.game.me)
  const players = useSelector(state => state.game.players)
  const order = useSelector(state => state.game.order)

  function onClick(order) {
    dispatch(selectOrder(order))
  }

  function orderList(players) {
    let arr = ((players.maxUser / 2) === 5) ? [<Grid item xs={1}></Grid>] : [];
    for (let i = 0; i < (players.maxUser / 2); i++) {
      arr.push(
        <Grid
          item
          xs={((players.maxUser / 2) === 5) ? 2 : (12 / (players.maxUser / 2))}
          key={i}
          sx={{ display: "flex", justifyContent: "center"}}
        >
          <div onClick={() => onClick(i)} style={{ width: 80, height: 80, backgroundColor: "#e2e2e2"}}>
            {i + 1}
            {order[me.team] ? (order[me.team][i] ? <Player player={order[me.team][i]}></Player> : null) : null}
          </div>
        </Grid>
      );
    }
    return arr;
  }

  return <Grid container>{orderList(players)}</Grid>;
}

export default OrderPicker;
