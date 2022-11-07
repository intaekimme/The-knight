import React from "react";

export default function LoginCheck(){
  if (window.localStorage.getItem("loginToken")) {
    return true;
  } else {
    return false;
  }
  // return true; // 로그인체크 없이 테스트 할때
}