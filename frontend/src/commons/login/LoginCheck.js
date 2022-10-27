import React from "react";
import { useSelector } from "react-redux";

export default function LoginCheck(){
  return useSelector(state => state.login.value.isLogin);
  // return true; // 로그인체크 없이 테스트 할때
}