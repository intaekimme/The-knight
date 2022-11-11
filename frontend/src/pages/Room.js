import React from "react";
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { Grid } from "@mui/material";

import RoomDisplay from "../components/room/RoomDisplay";
import Chatting from "../commons/chatting/Chatting";
import {onPubEnterRoom} from "../websocket/RoomPublishes";

export default function Room() {
	const windowData = useSelector((state) => state.windowData.value);
	const stompClient = useSelector((state) => state.websocket.stompClient);
	const gameId = useParams("gameId").gameId;
	React.useEffect(()=>{
		onPubEnterRoom({stompClient:stompClient, gameId:gameId});
	}, []);
  return (
		<Grid container>
			<Grid item xs={windowData.mainGridWidth}><RoomDisplay /></Grid>
			<Grid item xs={windowData.chatGridWidth}><Chatting /></Grid>
    </Grid>
  );
}