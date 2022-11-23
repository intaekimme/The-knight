import * as React from 'react';
import { useSelector } from 'react-redux';
import SearchForm from '../components/rank/SearchForm';
import RankTable from "../components/rank/RankTable";

import { Container } from "@mui/system";
import "../_css/Rank.module.css"
import styled from "../_css/Rank.module.css"
import { onPubExit } from "../websocket/RoomPublishes";

export default function Rank() {
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const gameId = useSelector(state=>state.room.roomInfo.gameId);
  console.log(stompClient);
  if(stompClient){
    onPubExit({stompClient:stompClient, gameId:gameId});
    console.log("disconnect");
    stompClient.disconnect();
  }
  return (
    <div className={styled.imgRank}>
    <Container fixed >
      <br/>
      <SearchForm />
      <br />
      <RankTable />
      {/* <Table/> */}
      <br/>
    </Container>
    </div>
  );
}