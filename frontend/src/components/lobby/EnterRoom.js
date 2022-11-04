import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { over } from "stompjs";
import SockJS from "sockjs-client";
import {connect, enterRoom} from '../../_slice/websocketSlice';
import {setRoom, setUsers} from '../../_slice/roomSlice';
import api from '../../api/api';
import {onModifyRoom, onState, onChatAll, onChatTeam, onEnterRoom,
  onAllMembersInRoom, onSelectTeam, onReady, onExitRoom} from '../../websocket/Receivers';

export default function EnterRoom(){
  const gameId = useParams("gameId").gameId;
  console.log(gameId);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  dispatch(connect());
  // console.log(client);
  // alert(client);

  const payload = {
    navigate: navigate,
    url: "/in-room",
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
  console.log(payload);
  dispatch(enterRoom(payload));
}