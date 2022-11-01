import React from "react";
import { useState } from "react";
import MemberInfoForm from '../components/memberPage/MemberInfoForm'
import CurrentRecord from '../components/memberPage/CurrentRecord'
import UpdateMemInfo from '../components/memberPage/UpdateMemInfo';

// import { useNavigate } from 'react-router-dom';
// import LoginCheck from "../commons/login/LoginCheck";

import { Container } from "@mui/system";
import { Grid } from "@mui/material";

export default function MemberPage() {
  // const isLogin = LoginCheck();
  // const navigate = useNavigate();
  // React.useEffect(()=>{
  //   if(!isLogin){
  //     navigate('/login');
  //   }
  // }, []);
  const [clickUpdate, setClickUpdate] = useState(false);
  const updateProfile = () => {
      console.log("this");
      clickUpdate === true ? (
        setClickUpdate(false)
      ) : (
      setClickUpdate(true)
      )
  }
  return (
    <Container fixed>
      {!clickUpdate
        ?
        <Grid>
          <MemberInfoForm updateProfile={updateProfile} />
          <CurrentRecord />
        </Grid>
      :
      <UpdateMemInfo />
      }
    </Container>
  );
}