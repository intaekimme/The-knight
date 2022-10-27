import { useSelector } from "react-redux";
import Grid from "@mui/material/Grid";

function OrderPicker() {
  const players = useSelector(state => state.players.value)

  function orderList(players) {
    let arr = [];
    if (players.peopleNum === 5) {
      arr.push(
        <Grid item xs={1}></Grid>
      )
    }
    for (let i = 1; i < players.peopleNum + 1; i++) {
      arr.push(
        <Grid
          item
          xs={(players.peopleNum === 5) ? 2 : (12 / players.peopleNum)}
          key={players.teamA[`player${i}`].memberId}
        >
          <div style={{ width: 80, height: 80, backgroundColor: "#e2e2e2" }}></div>
        </Grid>
      );
    }
    return arr;
  }

  return <Grid container>{orderList(players)}</Grid>;
}

export default OrderPicker;
