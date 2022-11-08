import React, {useCallback} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { over } from "stompjs";
import SockJS from "sockjs-client";
import {enterRoomSubscribe} from '../../_slice/websocketSlice';
import {setRoom, setUsers} from '../../_slice/roomSlice';
import api from '../../api/api';
import {onModifyRoom, onState, onChatAll, onChatTeam, onEnterRoom,
  onAllMembersInRoom, onSelectTeam, onReady, onExitRoom} from '../../websocket/Receivers';

export default function EnterRoom(){
  const gameId = useParams("gameId").gameId;
  console.log(gameId);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const stompClient = useSelector(state=>state.websocket.stompClient);
  const payload = {
    stompClient: stompClient,
    subscribes : [{
      api: api.subModifyRoom(gameId),
      receiver : onModifyRoom,
    },{
      api: api.subState(gameId),
      receiver : onState,
    },{
      api: api.subChatAll(gameId),
      receiver : onChatAll,
    },{
      api: api.subChatTeam(gameId, 'a'),
      receiver : onChatTeam,
    },{
      api: api.subEnterRoom(gameId),
      receiver : onEnterRoom,
    },{
      api: api.subAllMembersInRoom(gameId),
      receiver : onAllMembersInRoom,
    },{
      api: api.subSelectTeam(gameId),
      receiver : onSelectTeam,
    },{
      api: api.subReady(gameId),
      receiver : onReady,
    },{
      api: api.subExitRoom(gameId),
      receiver : onExitRoom,
    },],
  }
  const url = '/in-room';
  dispatch(enterRoomSubscribe(payload)).then((response)=>{
    console.log(response);
    navigate(url);
  });
}