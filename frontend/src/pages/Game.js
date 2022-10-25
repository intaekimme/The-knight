import React from "react";
import { useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';
import LoginCheck from "../commons/login/LoginCheck";

export default function Game() {
  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(()=>{
    if(!isLogin){
      navigate('/login');
    }
  }, []);
  return (
    <div>
      게임
    </div>
  );
}