import React from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { fetchPlayers } from '../_slice/gameSlice';
import { over } from "stompjs";
import SockJS from "sockjs-client";

import LoginCheck from "../commons/login/LoginCheck";
import Phase0 from "../components/game/Phase0";
import Phase1 from "../components/game/Phase1";

export default function Game() {
  // 비 로그인 시 로그인 화면으로
  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(() => {
    if (!isLogin) {
      navigate("/login");
    }
  }, []);

  const dispatch = useDispatch();

  const Sock = new SockJS("http://localhost:8080/websocket");
  const stompClient = over(Sock);
  stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, onConnectError)

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

  const phase = useSelector(state => state.game.phase)
  
  return (
    <div>
      {phase === 0 ? <Phase0></Phase0> : null}
      {phase === 1 ? <Phase1></Phase1> : null}
    </div>
  );
}