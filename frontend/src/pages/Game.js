import React from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { resetGameSlice } from "../_slice/gameSlice";
import { fetchMemberInfo } from "../_slice/memberInfoSlice";

import styled from "../_css/Game.module.css";
import LoginCheck from "../commons/login/LoginCheck";
import LoadingPhase from "../components/game/LoadingPhase";
import PreparePhase from "../components/game/PreparePhase";
import PredecessorPhase from "../components/game/PredecessorPhase";
import AttackPhase from "../components/game/AttackPhase";
import AttackDoubtPhase from "../components/game/AttackDoubtPhase";
import DefensePhase from "../components/game/DefensePhase";
import DefenseDoubtPhase from "../components/game/DefenseDoubtPhase";
import DoubtResultPhase from "../components/game/DoubtResultPhase";
import ExecutePhase from "../components/game/ExecutePhase";
import EndPhase from "../components/game/EndPhase";
import GameWebSocket from "../components/game/GameWebSocket";
import Chatting from "../commons/modal/chatting/Chatting";

import { createTheme, ThemeProvider } from '@mui/material/styles';

export default function Game() {
	const dispatch = useDispatch();
	const stompClient = useSelector((state) => state.websocket.stompClient);
	const gameId = useSelector((state) => state.room.roomInfo.gameId);
	const size = 35;

  const theme = createTheme({
    palette: {
      dark: {
        main: '#000000',
        // contrastText: '#fff',
      },
    },
  });

  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(() => {
    // 비 로그인 시 로그인 화면으로
    if (!isLogin) {
      navigate("/login");
    }
    // 게임데이터 초기화
    dispatch(resetGameSlice())
    // 유저 정보 갱신 (score 사용)
    dispatch(fetchMemberInfo());
  }, []);

  const phase = useSelector(state => state.game.phase)
  
  return (
    <div className={styled.imgGame}>
      <ThemeProvider theme={theme}>
        <GameWebSocket></GameWebSocket>
        {phase === "LOADING" && <LoadingPhase></LoadingPhase>}
        {phase === "PREPARE" && <PreparePhase></PreparePhase>}
        {phase === "PREDECESSOR" && <PredecessorPhase></PredecessorPhase>}
        {phase === "ATTACK" && <AttackPhase></AttackPhase>}
        {phase === "ATTACK_DOUBT" && <AttackDoubtPhase></AttackDoubtPhase>}
        {phase === "DEFENSE" && <DefensePhase></DefensePhase>}
        {phase === "DEFENSE_DOUBT" && <DefenseDoubtPhase></DefenseDoubtPhase>}
        {phase === "DOUBT_RESULT" && <DoubtResultPhase></DoubtResultPhase>}
        {phase === "EXECUTE" && <ExecutePhase></ExecutePhase>}
        {phase === "END" && <EndPhase></EndPhase>}
        <Chatting size={size} stompClient={stompClient} gameId={gameId}/>
      </ThemeProvider>
    </div>
  );
}