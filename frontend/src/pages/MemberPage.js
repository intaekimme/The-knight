import React from "react";
import MemberInfoForm from '../components/memberPage/MemberInfoForm'
import CurrentRecord from '../components/memberPage/CurrentRecord'

// import { useNavigate } from 'react-router-dom';
// import LoginCheck from "../commons/login/LoginCheck";

import { Container } from "@mui/system";

export default function MemberPage() {
  // const isLogin = LoginCheck();
  // const navigate = useNavigate();
  // React.useEffect(()=>{
  //   if(!isLogin){
  //     navigate('/login');
  //   }
  // }, []);
  return (
    <Container fixed>
      <MemberInfoForm />
      <CurrentRecord/>
    </Container>
  );
}