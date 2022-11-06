import React from "react";
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux'
import { login } from "../../_slice/loginSlice";

export default function LoginToken() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const loginToken = useSelector((state) => state.login.value.token);
  // store에 저장
  const mounted = async () => {
    console.log();
    const token = new URLSearchParams(window.location.search).get("token");
    const memberId = new URLSearchParams(window.location.search).get("memberId");
    dispatch(login(token));
    window.localStorage.setItem("loginToken", token);
    window.localStorage.setItem("memberId", memberId);
    navigate("/");
  };
  React.useEffect(() => {
    mounted();
  }, []);

  return <div>로그인!! {loginToken}</div>;
}
