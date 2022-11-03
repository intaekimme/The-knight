import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { over } from "stompjs";
import SockJS from "sockjs-client";
import api from '../../api/api';
import {connect, enterRoom} from '../../_slice/websocketSlice';
import {setRoom, setUsers} from '../../_slice/roomSlice';

export default async function EnterRoom(gameId){
  const dispatch = useDispatch();
  dispatch(connect());

  // 방 입장 리시버
  const onEnterRoom = (payload) => {
    const data = JSON.parse(payload.body);
    // {
    //   memberId : long,
    //   nickname: String,
    //   image: String
    // }
  };
  // 방 전체 멤버 리시버
  const onAllMembersInRoom = (payload) => {
    const data = JSON.parse(payload.body);
    // {
    //   maxUser : int (2 vs 2 → 4),
    //   members : [
    //     {
    //       id : long,
    //       nickname : String,
    //       team: String,
    //     },
    //     {
    //     }, 
    //     …
    //   ]
    // }
  };
  // 방 퇴장 리시버
  const onExitRoom = (payload) => {
    const data = JSON.parse(payload.body);
    // {
    //   memberId: long
    // }
  };
  // 팀선택 리시버
  const onSelectTeam = (payload) => {
    const data = JSON.parse(payload.body);
    // {
    //   memberId : long,
    //   team: String
    //   (A, B)
    // }
  };
  // ready 리시버
  const onReady = (payload) => {
    const data = JSON.parse(payload.body);
    // {
    //   players : [
    //     {
    //       id : long,
    //       readyStatus: boolean
    //       startFlag : boolean
    //     }, …
    //   ],
    //   setGame : String
    // }
  };


  const isConnect = await useSelector((state) => state.connect);
  const isFail = await useSelector((state) => state.fail);
  while(!isConnect && !isFail){
    const temp = await console.log("not yet");
  }
  // state.stompClient.subscribe(api.enterRoom(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.allMembersInRoom(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.exitRoom(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.selectTeam(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
      // state.stompClient.subscribe(api.ready(action.payload.gameId),
      //   onMessageReceived, (error) => {console.log(error);});
  const payload = {
    gameId: gameId,
    apis: [api.enterRoom, api.allMembersInRoom, api.exitRoom, api.selectTeam, api.ready],
    receivers: [onEnterRoom, onAllMembersInRoom, onExitRoom, onSelectTeam, onReady],
  }
  dispatch(enterRoom(gameId));

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