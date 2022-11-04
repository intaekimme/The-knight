import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { over } from "stompjs";
import SockJS from "sockjs-client";
import {connect, enterRoom} from '../../_slice/websocketSlice';
import {setRoom, setUsers} from '../../_slice/roomSlice';
import api from '../../api/api';
import {onModifyRoom, onState, onChatAll, onChatTeam, onEnterRoom,
  onAllMembersInRoom, onSelectTeam, onReady, onExitRoom} from '../../websocket/Receivers';

export default function EnterRoom(){
  const gameId = new URLSearchParams(window.location.search).get("gameId");
  const dispatch = useDispatch();
  const navigate = useNavigate();
  dispatch(connect());

  // // 방 설정 변경 리시버
  // const onModifyRoom = (payload) =>{
  //   // {
  //   //   title: String,
  //   //   maxUser: int,
  //   //   sword: int,
  //   //   twin: int,
  //   //   shield: int,
  //   //   hand: int
  //   // }
  // }
  // // 현재 진행상태 리시버
  // const onState = (payload) =>{
  //   // {
  //   //   state : String (Enum)
  //   // }
  // }
  // // 전체 채팅 리시버
  // const onChatAll = (payload) => {
  //   // {
  //   //   memberId : long,
  //   //   nickname : String,
  //   //   content : String,
  //   //   chattingSet : String
  //   //    (ALL, A, B)
  //   // }
  //   const data = JSON.parse(payload.body);
  //   const text = `${data.nickname} : ${data.content}`;
  //   // 내 채팅일 때 오른쪽에 표시
  //   if(data.memberId === window.localStorage.getItem("memberId")){
  //     console.log(text);
  //   }
  //   // 내 채팅이 아닐 때 왼쪽에 표시
  //   else{
  //     console.log(text);
  //   }
  // }
  // // 팀 채팅 리시버
  // const onChatTeam = (payload) => {
  //   // {
  //   //   memberId : long,
  //   //   nickname : String,
  //   //   content : String,
  //   //   chattingSet : String
  //   //    (ALL, A, B)
  //   // }
  //   const data = JSON.parse(payload.body);
  //   const text = `${data.nickname} : ${data.content}`;
  //   // 내 채팅일 때 오른쪽에 표시
  //   if(data.memberId === window.localStorage.getItem("memberId")){
  //     console.log(text);
  //   }
  //   // 내 채팅이 아닐 때 왼쪽에 표시
  //   else{
  //     console.log(text);
  //   }
  // }
  // // 방 입장 리시버
  // const onEnterRoom = (payload) => {
  //   // {
  //   //   memberId : long,
  //   //   nickname: String,
  //   //   image: String,
  //   //   image: String
  //   // }
  //   const data = JSON.parse(payload.body);
  //   const text = `${data.nickname}님이 입장하셨습니다.`;
  //   // 전체채팅으로 뿌려주기
  //   console.log(text);
  // };
  // // 방 전체 멤버 리시버
  // const onAllMembersInRoom = (payload) => {
  //   const data = JSON.parse(payload.body);
  //   // {
  //   //   maxUser : int (2 vs 2 → 4),
  //   //   members : [
  //   //     {
  //   //       id : long,
  //   //       nickname : String,
  //   //       image: String,
  //   //       team: String,
  //   //     },
  //   //     {
  //   //     }, 
  //   //     …
  //   //   ]
  //   // }
  // };
  // // 팀선택 리시버
  // const onSelectTeam = (payload) => {
  //   const data = JSON.parse(payload.body);
  //   // {
  //   //   memberId : long,
  //   //   team: String
  //   //   (A, B)
  //   // }
  // };
  // // ready 리시버
  // const onReady = (payload) => {
  //   const data = JSON.parse(payload.body);
  //   // {
  //   //   players : [
  //   //     {
  //   //       id : long,
  //   //       readyStatus: boolean
  //   //       startFlag : boolean
  //   //     }, …
  //   //   ],
  //   //   setGame : String
  //   // }
  // };
  // // 방 퇴장 리시버
  // const onExitRoom = (payload) => {
  //   const data = JSON.parse(payload.body);
  //   // {
  //   //   memberId: long
  //   // }
  // };

  // const isConnect = await useSelector((state) => state.connect);
  // const isFail = await useSelector((state) => state.fail);
  // while(!isConnect && !isFail){
  //   const temp = await console.log("not yet");
  // }
  const payload = {
    gameId: gameId,
    subscribes : [{
      api: api.subModifyRoom,
      receiver : onModifyRoom,
    },{
      api: api.subState,
      receiver : onState,
    },{
      api: api.subChatAll,
      receiver : onChatAll,
    },{
      api: api.subChatTeam,
      receiver : onChatTeam,
    },{
      api: api.subEnterRoom,
      receiver : onEnterRoom,
    },{
      api: api.subAllMembersInRoom,
      receiver : onAllMembersInRoom,
    },{
      api: api.subSelectTeam,
      receiver : onSelectTeam,
    },{
      api: api.subReady,
      receiver : onReady,
    },{
      api: api.subExitRoom,
      receiver : onExitRoom,
    },],
  }
  dispatch(enterRoom(payload));
  navigate(`/room`);

  // // config/WebsocketConfig.java registerStompEndpoints 를 websocket/pub/sub로 설정했기 때문에 마지막에 websocket/pub/sub 있어야함
  // console.log(window.localStorage.getItem("loginToken"));
  // let Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
  // stompClient = over(Sock);
  // stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, (error) => {
  //   console.log(error);
  // });
  // // const [members, setMembers] = useState({
  // //   sender: "",
  // //   receiver: "ALL", // ALL / A / B
  // //   connected: false,
  // //   message: "",
  // // });
  // const [connected, setConnected] = useState(false);
  // const [members, setMembers] = useState([{
  //     id : -1,
  //     nickname : "닉네임",
  //     team: "A", // A / B
  //   },
  // ]);

  // const onConnected = () => {
  //   setConnected(true);
  //   // setUserData({ ...userData, connected: true });
  //   stompClient.subscribe(api.enterRoom(gameId), onMessageReceived, (error) => {
  //     console.log(error);
  //   });
  //   userJoin();
  // };

  // const userJoin = () => {
  //   var chatMessage = {
  //     sender: userData.sender,
  //     status: "JOIN",
  //   };
  //   stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
  // };

  // const onMessageReceived = (payload) => {
  //   var payloadData = JSON.parse(payload.body);
  //   switch (payloadData.status) {
  //     case "JOIN":
  //       if (!privateChats.get(payloadData.sender)) {
  //         privateChats.set(payloadData.sender, []);
  //         setPrivateChats(new Map(privateChats));
  //       }
  //       break;
  //     case "MESSAGE":
  //       publicChats.push(payloadData);
  //       setPublicChats([...publicChats]);
  //       break;
  //     default:
  //       console.log("default");
  //       break;
  //   }
  // };
}