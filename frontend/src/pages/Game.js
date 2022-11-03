import React from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import LoginCheck from "../commons/login/LoginCheck";
import LoadingPhase from "../components/game/LoadingPhase";
import WeaponSelectPhase from "../components/game/WeaponSelectPhase";
import ActionSelectPhase from "../components/game/ActionSelectPhase";

export default function Game() {
  // // 비 로그인 시 로그인 화면으로
  // const isLogin = LoginCheck();
  // const navigate = useNavigate();
  // React.useEffect(() => {
  //   if (!isLogin) {
  //     navigate("/login");
  //   }
  // }, []);

  const phase = useSelector(state => state.game.phase)
  
  return (
    <div>
      {phase === 0 ? <LoadingPhase></LoadingPhase> : null}
      {phase === 1 ? <WeaponSelectPhase></WeaponSelectPhase> : null}
      {phase === 2 ? <ActionSelectPhase></ActionSelectPhase> : null}
    </div>
  );
}