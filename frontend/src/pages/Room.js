import React from "react";
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { Grid } from "@mui/material";

import styled from "../_css/Room.module.css";
import RoomDisplay from "../components/room/RoomDisplay";
import Chatting from "../commons/modal/chatting/Chatting";
import { onPubEntry } from "../websocket/RoomPublishes";

export default function Room() {
	const windowData = useSelector((state) => state.windowData.value);
	const stompClient = useSelector((state) => state.websocket.stompClient);
	const gameId = useParams("gameId").gameId;
	const size = 25;
	React.useEffect(() => {
		onPubEntry({ stompClient: stompClient, gameId: gameId });
	}, []);
	return (
		<div className={styled.imgRoom}>
			<Grid container>
				<Grid item xs={12}><RoomDisplay size={size} /></Grid>
				<Chatting size={size} stompClient={stompClient} gameId={gameId} />
			</Grid>
		</div>
	);
}