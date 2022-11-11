import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {enterRoomSubscribe, exitRoomUnsubscribe} from '../../_slice/websocketSlice';
import api from '../../api/api';
// import {onSubModifyRoom, onSubState, onSubChatAll, onSubChatTeam, onSubEntry,
//   onSubMembers, onSubSelectTeam, onSubReady, onSubExit} from '../../websocket/RoomReceivers';
import { getRoomInfo, modifyRoomSetting, setState, setMembers, changeTeam, changeReady } from '../../_slice/roomSlice';
import { onPubMembers } from '../../websocket/RoomPublishes';

export default function EnterRoom(){
  const navigate = useNavigate();
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const gameId = useParams("gameId").gameId;
  console.log(gameId);
  const dispatch = useDispatch();

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
    dispatch(modifyRoomSetting(data));
  };
  // 현재 진행상태 리시버
  const onSubState = (payload) => {
    // {
    //   state : String (Enum)
    // }
    const data = JSON.parse(payload.body);
    console.log("현재 진행상태 sub", data);
    dispatch(setState(data));
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
  const onSubEntry = (payload) => {
    // {
    //   memberId : long,
    //   nickname: String,
    //   image: String,
    // }
    const data = JSON.parse(payload.body);
    const text = `${data.nickname}님이 입장하셨습니다.`;
    onPubMembers({stompClient:stompClient, gameId:gameId});
    // 전체채팅으로 뿌려주기
    console.log(text);
    // 전체 멤버 publish
    console.log("방 입장 sub", data);
  };
  // 방 전체 멤버 리시버
  const onSubMembers = (payload) => {
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
    dispatch(setMembers(data.members));
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
    dispatch(changeTeam(data));
  };
  // ready 리시버
  const onSubReady = (payload) => {
    // {
    //   memberId: long,
    //   readyStatus : boolean,
    // }
    const data = JSON.parse(payload.body);
    console.log("레디 sub", data);
    dispatch(changeReady(data.readyResponseDto));
  };
  // 방 퇴장 리시버
  const onSubExit = (payload) => {
    // {
    //   memberId: long
    //   nickname: long
    // }
    const data = JSON.parse(payload.body);
    const text = `${data.nickname}님이 퇴장하셨습니다.`;
    if(data.memberId.toString()===window.localStorage.getItem("memberId")){
      dispatch(exitRoomUnsubscribe({stompClient:stompClient, gameId:gameId})).then(()=>{
        navigate('/lobby');
      }).catch((err)=>{
        console.log(err);
      });
    }
    else{
      onPubMembers({stompClient:stompClient, gameId:gameId});
      // 전체채팅으로 뿌려주기
      console.log(text);
      // 전체 멤버 publish
      console.log("방 퇴장 sub", data);
    }
  };

  // subscribe data
  const payload = {
    stompClient: stompClient,
    subscribes : [{
      api: api.subModifyRoom(gameId),
      receiver : onSubModifyRoom,
      id: "modifyRoom",
    },{
      api: api.subState(gameId),
      receiver : onSubState,
      id: "state",
    },{
      api: api.subChatAll(gameId),
      receiver : onSubChatAll,
      id: "chatAll",
    },{
      api: api.subChatTeam(gameId, 'a'),
      receiver : onSubChatTeam,
      id: "chatTeam",
    },{
      api: api.subEntry(gameId),
      receiver : onSubEntry,
      id: "entry",
    },{
      api: api.subMembers(gameId),
      receiver : onSubMembers,
      id: "members",
    },{
      api: api.subSelectTeam(gameId),
      receiver : onSubSelectTeam,
      id: "selectTeam",
    },{
      api: api.subReady(gameId),
      receiver : onSubReady,
      id: "ready",
    },{
      api: api.subExit(gameId),
      receiver : onSubExit,
      id: "exit",
    },],
  }
  const url = `/in-room/${gameId}`;
  const [isSetting, setIsSetting] = React.useState(false);
  React.useEffect(()=>{
    if(isSetting){
      setIsSetting(false);
      navigate(url);
    }
  },[isSetting]);
  
  React.useEffect(()=>{
    dispatch(enterRoomSubscribe(payload)).then((response)=>{
      console.log(response);
      //room 정보 요청 후 update
      dispatch(getRoomInfo(gameId)).then((response)=>{
        console.log(response);
        setIsSetting(true);
      }).catch((err)=>{console.log("room 정보 불러오기 실패", err); navigate("/");});
    }).catch((err)=>{console.log("room 입장 실패", err); navigate("/");});
  },[]);
}