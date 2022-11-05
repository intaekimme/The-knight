import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { modifyRoomSetting, setState, setUsers, changeTeam, changeReady } from '../_slice/roomSlice';

export default function Receivers(){
  const dispatch = useDispatch();
  // 방 설정 변경 리시버
  const onModifyRoom = (payload) =>{
    // {
    //   title: String,
    //   maxUser: int,
    //   sword: int,
    //   twin: int,
    //   shield: int,
    //   hand: int
    // }
    const data = JSON.parse(payload.body);
    console.log("방설정변경", data);
    dispatch(modifyRoomSetting(data));
  }
  // 현재 진행상태 리시버
  const onState = (payload) =>{
    // {
    //   state : String (Enum)
    // }
    const data = JSON.parse(payload.body);
    console.log("현재 진행상태", data);
    dispatch(setState(data));
  }
  // 전체 채팅 리시버
  const onChatAll = (payload) => {
    // {
    //   memberId : long,
    //   nickname : String,
    //   content : String,
    //   chattingSet : String
    //    (ALL, A, B)
    // }
    const data = JSON.parse(payload.body);
    const text = `${data.nickname} : ${data.content}`;
    // 내 채팅일 때 오른쪽에 표시
    if(data.memberId === window.localStorage.getItem("memberId")){
      console.log(text);
    }
    // 내 채팅이 아닐 때 왼쪽에 표시
    else{
      console.log(text);
    }
  }
  // 팀 채팅 리시버
  const onChatTeam = (payload) => {
    // {
    //   memberId : long,
    //   nickname : String,
    //   content : String,
    //   chattingSet : String
    //    (ALL, A, B)
    // }
    const data = JSON.parse(payload.body);
    const text = `${data.nickname} : ${data.content}`;
    // 내 채팅일 때 오른쪽에 표시
    if(data.memberId === window.localStorage.getItem("memberId")){
      console.log(text);
    }
    // 내 채팅이 아닐 때 왼쪽에 표시
    else{
      console.log(text);
    }
  }
  // 방 입장 리시버
  const onEnterRoom = (payload) => {
    // {
    //   memberId : long,
    //   nickname: String,
    //   image: String,
    //   image: String
    // }
    const data = JSON.parse(payload.body);
    const text = `${data.nickname}님이 입장하셨습니다.`;
    // 전체채팅으로 뿌려주기
    console.log(text);
    // 전체 멤버 publish
  };
  // 방 전체 멤버 리시버
  const onAllMembersInRoom = (payload) => {
    // {
    //   members : [
    //     {
    //       id : long,
    //       nickname : String,
    //       image: String,
    //       team: String,
    //     },
    //     {
    //     }, 
    //     …
    //   ]
    // }
    const data = JSON.parse(payload.body);
    console.log("전체 멤버 조회", data);
    setUsers(data.members);
  };
  // 팀선택 리시버
  const onSelectTeam = (payload) => {
    // {
    //   memberId : long,
    //   team: String
    //   (A, B)
    // }
    const data = JSON.parse(payload.body);
    console.log("팀선택", data);
    changeTeam(data);
  };
  // ready 리시버
  const onReady = (payload) => {
    // {
    //   memberId: long,
    //   readyStatus : boolean,
    //   startFlag??
    //   setGame,
    // }
    const data = JSON.parse(payload.body);
    console.log("레디", data);
    changeReady(data);
  };
  // 방 퇴장 리시버
  const onExitRoom = (payload) => {
    // {
    //   memberId: long
    //   nickname: long
    // }
    const data = JSON.parse(payload.body);
    const text = `${data.nickname}님이 퇴장하셨습니다.`;
    // 전체채팅으로 뿌려주기
    console.log(text);
    // 전체 멤버 publish
  };
}
export const {onModifyRoom, onState, onChatAll, onChatTeam, onEnterRoom, onAllMembersInRoom, onSelectTeam, onReady, onExitRoom} = Receivers;