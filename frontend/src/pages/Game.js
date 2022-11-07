import React from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import LoginCheck from "../commons/login/LoginCheck";
import LoadingPhase from "../components/game/LoadingPhase";
import WeaponSelectPhase from "../components/game/WeaponSelectPhase";
import ActionSelectPhase from "../components/game/ActionSelectPhase";

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
      {isLoading && <LoadingPhase></LoadingPhase>}
      {!isLoading && phase === "PRE" && <WeaponSelectPhase></WeaponSelectPhase>}
      {!isLoading && phase === "ATTACK" && <ActionSelectPhase></ActionSelectPhase>}
    </div>
  );
}