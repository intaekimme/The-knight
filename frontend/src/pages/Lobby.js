import React from "react";
import { useSelector } from 'react-redux';
// import { useNavigate } from 'react-router-dom';
// import LoginCheck from "../commons/login/LoginCheck";
import SearchBar from "../components/lobby/SearchBar";
import GameList from "../components/lobby/GameList";
import Table from "../components/test/table"
import styled from "../_css/Lobby.module.css"

import { Container } from "@mui/material";
import { onPubExit } from "../websocket/RoomPublishes";

export default function Lobby() {
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const gameId = useSelector(state=>state.room.roomInfo.gameId);
  console.log(stompClient);
  if(stompClient){
    onPubExit({stompClient:stompClient, gameId:gameId});
    console.log("disconnect");
    stompClient.disconnect();
  }
  // const isLogin = LoginCheck();
  // const navigate = useNavigate();
  // React.useEffect(()=>{
  //   if(!isLogin){
  //     navigate('/login');
  //   }
  // }, []);
  return (
    <div className={styled.imgLobby}>
    <Container fixed >
      <SearchBar />
      <GameList/>
      {/* <Table/> */}
    </Container>
    </div>
  );
}