import { useSelector, useDispatch } from "react-redux";
import api from "../../api/api";
import Player from "./Player";
import { Grid, Box } from "@mui/material";

function OrderPicker() {
  const players = useSelector((state) => state.game.players);
  const order = useSelector((state) => state.game.order);
  const isSelectComplete = useSelector((state) => state.game.isSelectComplete);

  const stompClient = useSelector((state) => state.websocket.stompClient);
  const memberId = parseInt(window.localStorage.getItem("memberId"));
  const myTeam = useSelector((state) => state.game.me).team;
  const gameId = useSelector((state) => state.room.roomInfo).gameId;

  const playerSize = "10vmin"

  const onPubOrder = (payload) => {
    // {
    //   orderNumber : int
    // }
    const data = {
      orderNumber: payload,
    };
    stompClient.send(api.pubOrder(gameId, myTeam), {}, JSON.stringify(data));
  };

  function onClick(order) {
    onPubOrder(order + 1);
  }

  function orderList(players) {
    let arr = [];
    for (let i = 0; i < players.maxMember / 2; i++) {
      arr.push(
        <Grid
          item
          xs={players.maxMember / 2 === 5 ? 2 : 12 / (players.maxMember / 2)}
          key={i}
          sx={{ display: "flex", justifyContent: "center" }}
        >
          <Box
            onClick={() => onClick(i)}
            sx={{
              width: "12vmin",
              height: "12vmin",
              ...(isSelectComplete
                ? { backgroundColor: "#646464" }
                : { backgroundColor: "#f0f0f0" }),
              border: "5px solid #424242",
              borderLeftWidth: "4vmin",
              borderRadius: "10px",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              position: "relative",
            }}
          >
            <Box
              sx={{
                position: "absolute",
                left: "-2.7vmin",
                top: "50%",
                transform: "translate(0, -50%)",
                color: "white",
                fontSize: "2.5vmin"
              }}
            >
              {i + 1}
            </Box>
            {order[i] ? <Player player={order[i]} size={playerSize}></Player> : null}
          </Box>
        </Grid>
      );
    }
    return arr;
  }

  return <Grid container>{orderList(players)}</Grid>;
}

export default OrderPicker;
