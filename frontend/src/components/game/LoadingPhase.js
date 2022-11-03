import SockJS from "sockjs-client";
import { over } from "stompjs";
import { useDispatch } from "react-redux";
import { fetchPlayers } from '../../_slice/gameSlice';

import { CircularProgress, Box } from '@mui/material';

export default function LoadingPhase() {
  const Sock = new SockJS("http://localhost:8080/websocket");
  const stompClient = over(Sock);
  stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, onConnectError)

  const dispatch = useDispatch();

  function onConnected() {
    // gameId는 임의로 1
    stompClient.subscribe("/sub/games/1/members", onMessageReceived, (error) => {
      console.log("error", error);
    });
  }

  function onMessageReceived(payload) {
    const payloadData = JSON.parse(payload.body);
    dispatch(fetchPlayers(payloadData))
  }

  function onConnectError(error) {
    console.log(error);
  };

  return (
    <div>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
        }}
      >
        <CircularProgress />
        <div>
          연결 중입니다
        </div>
      </Box>
    </div>
  )
}