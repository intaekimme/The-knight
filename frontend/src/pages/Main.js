import React from "react";
import { useSelector } from 'react-redux';
import MainBtn from '../components/main/MainBtn'

import styled from '../_css/Main.module.css'
import { onPubExit } from "../websocket/RoomPublishes";

export default function Main() {
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const gameId = useSelector(state=>state.room.roomInfo.gameId);
  console.log(stompClient);
  if(stompClient){
    onPubExit({stompClient:stompClient, gameId:gameId});
    console.log("disconnect");
    stompClient.disconnect();
  }
  return (
    <div className={styled.imgMain}>
      <MainBtn />
    </div>
  );
}