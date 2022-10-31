import React from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useSpring, animated } from "react-spring";

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

  const props = useSpring({ to: { opacity: 1 }, from: { opacity: 0 }, config: { duration: 1500 } })
  const phase = useSelector(state => state.game.phase)

  return (
    <div>
      <animated.div style={props}>I will fade in</animated.div>
      {phase === 0 ? <Phase0></Phase0> : null}
      {phase === 1 ? <Phase1></Phase1> : null}
    </div>
  );
}