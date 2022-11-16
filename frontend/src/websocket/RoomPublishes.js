import React from 'react';
import api from '../api/api';

export default function Publishes(){
  
}
// 방 설정 변경 publish
const onPubModifyRoom = (payload) =>{
  // {
  //   title: String,
  //   maxMember: int
  //   sword: int,
  //   twin: int,
  //   shield: int,
  //   hand: int
  // }
  console.log(payload);
  payload.stompClient.send(`${api.pubModifyRoom(payload.roomData.gameId)}`, {}, JSON.stringify(payload.roomData));
  console.log("방설정변경 pub", payload);
}
// 채팅 publish
const onPubChat = (payload) => {
  // {
  //   content : String
  //   chattingSet : String
  //     (All, A, B)
  // }
  payload.stompClient.send(`${api.pubChat(payload.gameId)}`, {}, JSON.stringify(payload.message));
  console.log("채팅 pub", payload);
}
// 방 입장 publish
const onPubEntry = (payload) => {
  payload.stompClient.send(`${api.pubEntry(payload.gameId)}`);
  console.log("방 입장 pub", payload);
};
// 방 전체 멤버 publish
const onPubMembers = (payload) => {
  payload.stompClient.send(`${api.pubMembers(payload.gameId)}`);
  console.log("방 전체 멤버 pub", payload);
};
// 팀선택 publish
const onPubSelectTeam = (payload) => {
  // {
  //   team: String
  //   (A, B)
  // }
  payload.stompClient.send(`${api.pubSelectTeam(payload.gameId)}`, {}, JSON.stringify({team: payload.team}));
  console.log("팀 선택 pub", payload);
};
// ready publish
const onPubReady = (payload) => {
  // {
  //   readyStatus : boolean
  // }
  payload.stompClient.send(`${api.pubReady(payload.gameId)}`, {}, JSON.stringify({ readyStatus: payload.ready }));
  console.log("ready pub", payload);
};
// 방 퇴장 publish
const onPubExit = (payload) => {
  payload.stompClient.send(`${api.pubExit(payload.gameId)}`);
  console.log("나가기 pub", payload);
};
export {onPubModifyRoom, onPubChat, onPubEntry, onPubMembers, onPubSelectTeam, onPubReady, onPubExit};