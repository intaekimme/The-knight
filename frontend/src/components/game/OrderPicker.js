import { useSelector, useDispatch } from "react-redux";
import { selectOrder } from "../../_slice/gameSlice"
import Player from "./Player"
import Grid from "@mui/material/Grid";

function OrderPicker() {
  const dispatch = useDispatch();
  const players = useSelector(state => state.game.players)
  const order = useSelector(state => state.game.order)

  function onClick(order) {
    dispatch(selectOrder(order))
  }

  function orderList(players) {
    let arr = (players.peopleNum === 5) ? [<Grid item xs={1}></Grid>] : [];
    for (let i = 1; i < players.peopleNum + 1; i++) {
      arr.push(
        <Grid
          item
          xs={(players.peopleNum === 5) ? 2 : (12 / players.peopleNum)}
          key={i}
        >
          <div onClick={() => onClick(i)} style={{ width: 80, height: 80, backgroundColor: "#e2e2e2" }}>
            {i}
            {/* 
              현재는 teamA의 순서를 보여주도록 고정 
              추후에 내 팀의 순서를 보여주도록 변경
            */}
            {order.teamA ? (order.teamA[i - 1] ? <Player isMe={true} userName={order.teamA[i-1].nickname}></Player> : null) : null}
          </div>
        </Grid>
      );
    }
    return arr;
  }

  return <Grid container>{orderList(players)}</Grid>;
}

export default OrderPicker;
