import React from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

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

export default function Game() {
  // 비 로그인 시 로그인 화면으로
  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(() => {
    if (!isLogin) {
      navigate("/login");
    }
  }, []);

  const isLoading = useSelector(state => state.game.isLoading)
  const phase = useSelector(state => state.game.phase)
  
  return (
    <div>
      <GameWebSocket></GameWebSocket>
      {isLoading && <LoadingPhase></LoadingPhase>}
      {!isLoading && phase === "PREPARE" && <PreparePhase></PreparePhase>}
      {!isLoading && phase === "PREDECESSOR" && <PredecessorPhase></PredecessorPhase>}
      {!isLoading && phase === "ATTACK" && <AttackPhase></AttackPhase>}
      {!isLoading && phase === "ATTACK_DOUBT" && <AttackDoubtPhase></AttackDoubtPhase>}
      {!isLoading && phase === "DEFENSE" && <DefensePhase></DefensePhase>}
      {!isLoading && phase === "DEFENSE_DOUBT" && <DefenseDoubtPhase></DefenseDoubtPhase>}
      {!isLoading && phase === "DOUBT_RESULT" && <DoubtResultPhase></DoubtResultPhase>}
      {!isLoading && phase === "EXECUTE" && <ExecutePhase></ExecutePhase>}
      {!isLoading && phase === "END" && <EndPhase></EndPhase>}
    </div>
  );
}