import React from "react";
import { useNavigate } from 'react-router-dom';
import LoginCheck from "../commons/login/LoginCheck";

export default function Lobby() {
  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(()=>{
    if(!isLogin){
      navigate('/login');
    }
  }, []);
  return (
    <div>
      로비
    </div>
  );
}