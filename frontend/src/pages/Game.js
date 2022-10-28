import React from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import LoginCheck from "../commons/login/LoginCheck";
import PlayerWithWeaponList from "../components/game/PlayerWithWeaponList";
import OrderPicker from "../components/game/OrderPicker";
import WeaponPicker from "../components/game/WeaponPicker";

export default function Game() {
  // // 비 로그인 시 로그인 화면으로
  // const isLogin = LoginCheck();
  // const navigate = useNavigate();
  // React.useEffect(() => {
  //   if (!isLogin) {
  //     navigate("/login");
  //   }
  // }, []);

  return (
    <div>
      <h1>진행 순서와 무기를 선택하세요</h1>
      <h3>100</h3>
      <div style={{ backgroundColor: "grey" }}>당신은 리더입니다.</div>
      <OrderPicker />
      <WeaponPicker></WeaponPicker>
      <PlayerWithWeaponList></PlayerWithWeaponList>
    </div>
  );
}
