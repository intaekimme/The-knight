import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { modifyRoomSetting, setState, setUsers, changeTeam, changeReady } from '../_slice/roomSlice';
// import { modifyRoomSetting, setState, setUsers, changeTeam, changeReady } from '../_slice/websocketSlice';
import api from '../api/api';

export default function Publishes(){
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const roomInfo = useSelector(state=>state.room.roomInfo);
  // 방 설정 변경 publish
  const onModifyRoom = (payload) =>{
    // {
    //   title: String,
    //   maxMember: int
    //   sword: int,
    //   twin: int,
    //   shield: int,
    //   hand: int
    // }
    stompClient.send(`${api.modifyRoom(payload.roomInfo.gameId)}`, {}, JSON.stringify(payload.roomInfo));
    console.log("방설정변경 pub", data);
  }
  // 채팅 publish
  const onChat = (payload) => {
    // {
    //   content : String
    //   chattingSet : String
    //     (All, A, B)
    // }
    stompClient.send(`${api.chat(payload.gameId)}`, {}, JSON.stringify(payload.chat));
    console.log("채팅 pub", data);
  }
  // 방 입장 publish
  const onEnterRoom = (payload) => {
    stompClient.send(`${api.chat(payload.gameId)}`, {}, {});
    console.log("채팅 pub", data);
  };
  // 방 전체 멤버 publish
  const onAllMembersInRoom = (payload) => {
    const data = JSON.parse(payload.body);
    console.log("전체 멤버 조회", data);
    setUsers(data.members);
  };
  // 팀선택 publish
  const onSelectTeam = (payload) => {
    // {
    //   team: String
    //   (A, B)
    // }
    const data = JSON.parse(payload.body);
    console.log("팀선택", data);
    changeTeam(data);
  };
  // ready publish
  const onReady = (payload) => {
    // {
    //   readyStatus : boolean
    // }
    const data = JSON.parse(payload.body);
    console.log("레디", data);
    changeReady(data);
  };
  // 방 퇴장 publish
  const onExitRoom = (payload) => {
    const data = JSON.parse(payload.body);
    const text = `${data.nickname}님이 퇴장하셨습니다.`;
    // 전체채팅으로 뿌려주기
    console.log(text);
    // 전체 멤버 publish
  };
}