import React from "react";
import { useNavigate } from 'react-router-dom';
import LoginCheck from "../commons/login/LoginCheck";

export default function UserPage() {
  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(()=>{
    if(!isLogin){
      navigate('/login');
    }
  }, []);
  return (
    <div>
      유저페이지
    </div>
  );
}