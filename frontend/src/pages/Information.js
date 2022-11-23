import React from "react";
import { useSelector } from 'react-redux';
import Info from '../components/info/Info'

import styled from '../_css/Info.module.css'
import { onPubExit } from "../websocket/RoomPublishes";

export default function Information() {
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const gameId = useSelector(state=>state.room.roomInfo.gameId);
  console.log(stompClient);
  if(stompClient){
    onPubExit({stompClient:stompClient, gameId:gameId});
    console.log("disconnect");
    stompClient.disconnect();
  }
  return (
    <div className={styled.imgInfo}>
      <Info />
    </div>
  );
}