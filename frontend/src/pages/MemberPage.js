import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from 'react-redux';

import LoginCheck from "../commons/login/LoginCheck";

import MemberInfoForm from '../components/memberPage/MemberInfoForm'
import CurrentRecord from '../components/memberPage/CurrentRecord'
import UpdateMemInfo from '../components/memberPage/UpdateMemInfo';

import "../_css/Mypage.module.css"
import { Container } from "@mui/system";
import { Grid } from "@mui/material";
import styled from "../_css/Mypage.module.css";
import { onPubExit } from "../websocket/RoomPublishes";

export default function MemberPage() {

  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(()=>{
    if(!isLogin){
      navigate('/login');
    }
  }, []);

  const stompClient = useSelector(state=>state.websocket.stompClient);
  const gameId = useSelector(state=>state.room.roomInfo.gameId);
  console.log(stompClient);
  if(stompClient){
    onPubExit({stompClient:stompClient, gameId:gameId});
    console.log("disconnect");
    stompClient.disconnect();
  }

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
    <>
      {
        !clickUpdate
          ?
          <div className={styled.imgMypage}>
            <Container fixed>
              <Grid sx={{ pb: 1 }}>
                <MemberInfoForm updateProfile={updateProfile} />

                <CurrentRecord />
              </Grid>
            </Container>
          </div>
          :
          <div className={styled.imgModi}>
            <Container fixed>
              <UpdateMemInfo />
            </Container>
          </div>
      }
    </>
  );
}