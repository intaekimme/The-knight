import SockJS from "sockjs-client";
import { over } from "stompjs";
import { useDispatch } from "react-redux";
import { fetchPlayers } from '../../_slice/gameSlice';
import api from "../../api/api"

import { CircularProgress, Box } from '@mui/material';

export default function LoadingPhase() {
  const Sock = new SockJS(api.websocket());
  const stompClient = over(Sock);
  stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, (error) => {
    console.log(error);
  })

  const dispatch = useDispatch();

  function onConnected() {
    console.log("연결됐다");
    // gameId는 임의로 1
    stompClient.subscribe(api.getAllPalyers(1), onMessageReceived);
  }

  function onMessageReceived(payload) {
    console.log("메시지 왔다");
    const payloadData = JSON.parse(payload.body);
    dispatch(fetchPlayers(payloadData))
  }

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