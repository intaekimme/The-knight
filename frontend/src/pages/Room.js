import React from "react";
import { useSelector } from 'react-redux';
import { Grid } from "@mui/material";

import RoomDisplay from "../components/room/RoomDisplay";
import Chatting from "../commons/chatting/Chatting";

export default function Room() {
	const windowData = useSelector((state) => state.windowData.value);
  return (
		<Grid container>
			<Grid item xs={windowData.mainGridWidth}><RoomDisplay /></Grid>
			<Grid item xs={windowData.chatGridWidth}><Chatting /></Grid>
    </Grid>
  );
}