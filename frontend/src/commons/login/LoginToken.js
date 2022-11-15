import React from "react";
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux'
import api from '../../api/api';
import { login } from "../../_slice/loginSlice";
import { fetchMemberInfo } from "../../_slice/memberInfoSlice";

export default function LoginToken() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const loginToken = useSelector((state) => state.login.value.token);
  // store에 저장
  const mounted = async () => {
    const token = new URLSearchParams(window.location.search).get("token");
    const memberId = new URLSearchParams(window.location.search).get("memberId");
    dispatch(login(token));
    window.localStorage.setItem("loginToken", token);
    window.localStorage.setItem("memberId", memberId);
    dispatch(fetchMemberInfo());

    console.log(token);
    // dispatch(connectWebsocket({token:token})).then((res)=>{alert("로그인 되었습니다");});
    navigate("/");
    // alert("로그인 되었습니다");
    // navigate("/");
  };
  React.useEffect(() => {
    mounted();
  }, []);

  return <div>로그인!! {loginToken}</div>;
}
