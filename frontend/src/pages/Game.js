import React from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import LoginCheck from "../commons/login/LoginCheck";
import Phase0 from "../components/game/Phase0";
import Phase1 from "../components/game/Phase1";

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
      {phase === 0 ? <Phase0></Phase0> : null}
      {phase === 1 ? <Phase1></Phase1> : null}
    </div>
  );
}