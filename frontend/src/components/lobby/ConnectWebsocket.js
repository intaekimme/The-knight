import React from "react";
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux'
import api from '../../api/api';
import { connectWebsocket } from '../../_slice/websocketSlice';

export default function ConnectWebsocket() {
  const gameId = useParams("gameId").gameId;
  const dispatch = useDispatch();
  const navigate = useNavigate();
  React.useEffect(()=>{
    const token = window.localStorage.getItem("loginToken");
    dispatch(connectWebsocket({token:token, navigate:navigate, url:api.routeEntryRoomSetting(gameId)})).then((response)=>{
      console.log("connect 성공");
    });
  }, []);
  return(
    <div/>
  );
}