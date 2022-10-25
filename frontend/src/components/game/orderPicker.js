import Grid from "@mui/material/Grid";

function OrderPicker({ dummyPlayers }) {

  function orderList(players) {
    let arr = [];
    for (let i = 0; i < (players.length/2); i++) {
      arr.push(
        // 5대5일 경우 추가
        <Grid
          item
          xs={12 / (players.length/2)}
          key={i}
        >
          <div style={{ width: 80, height: 80, backgroundColor: "#e2e2e2" }}></div>
        </Grid>
      );
    }
    return arr;
  }

  return <Grid container>{orderList(dummyPlayers)}</Grid>;
}

export default OrderPicker;
