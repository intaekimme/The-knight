import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {enterRoomSubscribe} from '../../_slice/websocketSlice';
import api from '../../api/api';
import {onSubModifyRoom, onSubState, onSubChatAll, onSubChatTeam, onSubEnterRoom,
  onSubAllMembersInRoom, onSubSelectTeam, onSubReady, onSubExitRoom} from '../../websocket/RoomReceivers';

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
  const url = `/in-room/${gameId}`;
  const [first, setFirst] = React.useState(true);
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
      setIsSetting(true);
    });
  },[]);
}