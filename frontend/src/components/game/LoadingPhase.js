import SockJS from "sockjs-client";
import { over } from "stompjs";
import { useSelector, useDispatch } from "react-redux";
import { fetchPlayers, fetchPhase, switchIsLoading } from '../../_slice/gameSlice';
import api from "../../api/api"

import { CircularProgress, Box } from '@mui/material';

export default function LoadingPhase() {
  const Sock = new SockJS(api.websocket());
  const stompClient = over(Sock);
  stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, (error) => {
    console.log(error);
  })

  const dispatch = useDispatch();

  const phase = useSelector(state => state.game.phase)

  function onConnected() {
    // gameId는 임의로 1

    // 연결이 되면 게임 전 준비(구독하기) - 다음 phase에 따라 구독할 큐 다름 
    if (phase === "PRE") {
      stompClient.subscribe(api.goLoading(1), onGoLoading)
      stompClient.subscribe(api.nextPhase(1), onNextPhase);
      // 다음 phase에서 필요한 구독 추가
    }

    // 게임 준비 후 시작 요청 보내기
    stompClient.send(api.readyForNextPhase(1), {}, JSON.stringify({status: phase}))
  }

  // function onMessageReceived(payload) {
  //   console.log("전체 사용자 정보", JSON.parse(payload.body));
  //   const payloadData = JSON.parse(payload.body);
  //   dispatch(fetchPlayers(payloadData))
  // }

  function onGoLoading(payload) {
    const payloadData = JSON.parse(payload.body);
    dispatch(fetchPhase(payloadData.status))
    dispatch(switchIsLoading())
  }

  function onNextPhase(payload) {
    const payloadData = JSON.parse(payload.body);
    dispatch(fetchPhase(payloadData.status))
    dispatch(switchIsLoading())
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