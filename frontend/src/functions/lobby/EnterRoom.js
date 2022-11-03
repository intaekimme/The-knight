import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { over } from "stompjs";
import SockJS from "sockjs-client";
import api from '../../api/api';
import {setRoom, setUsers} from '../../_slice/roomSlice';

export default function EnterRoom(gameId){
  // config/WebsocketConfig.java registerStompEndpoints 를 websocket/pub/sub로 설정했기 때문에 마지막에 websocket/pub/sub 있어야함
  console.log(window.localStorage.getItem("loginToken"));
  let Sock = new SockJS(`${api.websocket()}?token=${window.localStorage.getItem("loginToken")}`);
  stompClient = over(Sock);
  stompClient.connect({Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}, onConnected, (error) => {
    console.log(error);
  });
  // const [members, setMembers] = useState({
  //   sender: "",
  //   receiver: "ALL", // ALL / A / B
  //   connected: false,
  //   message: "",
  // });
  const [connected, setConnected] = useState(false);
  const [members, setMembers] = useState([{
      id : -1,
      nickname : "닉네임",
      team: "A", // A / B
    },
  ]);

  const connect = () => {
    
  };

  const onConnected = () => {
    setConnected(true);
    // setUserData({ ...userData, connected: true });
    stompClient.subscribe(api.enterRoom(gameId), onMessageReceived, (error) => {
      console.log(error);
    });
    userJoin();
  };

  const userJoin = () => {
    var chatMessage = {
      sender: userData.sender,
      status: "JOIN",
    };
    stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
  };

  const onMessageReceived = (payload) => {
    var payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
      case "JOIN":
        if (!privateChats.get(payloadData.sender)) {
          privateChats.set(payloadData.sender, []);
          setPrivateChats(new Map(privateChats));
        }
        break;
      case "MESSAGE":
        publicChats.push(payloadData);
        setPublicChats([...publicChats]);
        break;
      default:
        console.log("default");
        break;
    }
  };
}