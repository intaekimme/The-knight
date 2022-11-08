import React, {useCallback} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { over } from "stompjs";
import SockJS from "sockjs-client";
import {enterRoomSubscribe} from '../../_slice/websocketSlice';
import {setRoom, setUsers} from '../../_slice/roomSlice';
import api from '../../api/api';
import {onSubModifyRoom, onSubState, onSubChatAll, onSubChatTeam, onSubEnterRoom,
  onSubAllMembersInRoom, onSubSelectTeam, onSubReady, onSubExitRoom} from '../../websocket/Receivers';

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
      receiver : onSubModifyRoom,
    },{
      api: api.subState(gameId),
      receiver : onSubState,
    },{
      api: api.subChatAll(gameId),
      receiver : onSubChatAll,
    },{
      api: api.subChatTeam(gameId, 'a'),
      receiver : onSubChatTeam,
    },{
      api: api.subEnterRoom(gameId),
      receiver : onSubEnterRoom,
    },{
      api: api.subAllMembersInRoom(gameId),
      receiver : onSubAllMembersInRoom,
    },{
      api: api.subSelectTeam(gameId),
      receiver : onSubSelectTeam,
    },{
      api: api.subReady(gameId),
      receiver : onSubReady,
    },{
      api: api.subExitRoom(gameId),
      receiver : onSubExitRoom,
    },],
  }
  const url = '/in-room';
  dispatch(enterRoomSubscribe(payload)).then((response)=>{
    console.log(response);
    navigate(url);
  });
}