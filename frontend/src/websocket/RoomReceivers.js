import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { modifyRoomSetting, setState, setRoom, setMembers, changeTeam, changeReady } from '../_slice/roomSlice';

export default function Receivers(){
  
}

// 방 설정 변경 리시버
const onSubModifyRoom = (payload) => {
  // {
  //   title: String,
  //   maxMember: int
  //   sword: int,
  //   twin: int,
  //   shield: int,
  //   hand: int
  // }
  const data = JSON.parse(payload.body);
  console.log("방설정변경 sub", data);
  payload.dispatch(modifyRoomSetting(data));
};
// 현재 진행상태 리시버
const onSubState = (payload) => {
  // {
  //   state : String (Enum)
  // }
  const data = JSON.parse(payload.body);
  console.log("현재 진행상태 sub", data);
  payload.dispatch(setState(data));
};
// 전체 채팅 리시버
const onSubChatAll = (payload) => {
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
  if (data.memberId === window.localStorage.getItem("memberId")) {
    console.log(text);
  }
  // 내 채팅이 아닐 때 왼쪽에 표시
  else {
    console.log(text);
  }
  console.log("전채 채팅 sub", data);
};
// 팀 채팅 리시버
const onSubChatTeam = (payload) => {
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
  if (data.memberId === window.localStorage.getItem("memberId")) {
    console.log(text);
  }
  // 내 채팅이 아닐 때 왼쪽에 표시
  else {
    console.log(text);
  }
  console.log("팀 채팅 sub", data);
};
// 방 입장 리시버
const onSubEnterRoom = (payload) => {
  // {
  //   memberId : long,
  //   nickname: String,
  //   image: String,
  // }
  const data = JSON.parse(payload.body);
  const text = `${data.nickname}님이 입장하셨습니다.`;
  // 전체채팅으로 뿌려주기
  console.log(text);
  // 전체 멤버 publish
  console.log("방 입장 sub", data);
};
// 방 전체 멤버 리시버
const onSubAllMembersInRoom = (payload) => {
  // {
  //   members : [
  //     {
  //       id : long,
  //       nickname : String,
  //       image: String,
  //       team: String,
  //       readyStatus: Boolean,
  //     },
  //     {
  //     },
  //     …
  //   ]
  // }
  const data = JSON.parse(payload.body);
  // useDispatch(setMembers(data.members));
  console.log("전체 멤버 조회 sub", data);
};
// 팀선택 리시버
const onSubSelectTeam = (payload) => {
  // {
  //   memberId : long,
  //   team: String
  //   (A, B)
  // }
  const data = JSON.parse(payload.body);
  console.log("팀선택 sub", data);
  changeTeam(data);
};
// ready 리시버
const onSubReady = (payload) => {
  // {
  //   memberId: long,
  //   readyStatus : boolean,
  // }
  const data = JSON.parse(payload.body);
  console.log("레디 sub", data);
  changeReady(data);
};
// 방 퇴장 리시버
const onSubExitRoom = (payload) => {
  // {
  //   memberId: long
  //   nickname: long
  // }
  const data = JSON.parse(payload.body);
  const text = `${data.nickname}님이 퇴장하셨습니다.`;
  // 전체채팅으로 뿌려주기
  console.log(text);
  // 전체 멤버 publish
  console.log("방 퇴장 sub", data);
};
export {onSubModifyRoom, onSubState, onSubChatAll, onSubChatTeam, onSubEnterRoom, onSubAllMembersInRoom, onSubSelectTeam, onSubReady, onSubExitRoom};