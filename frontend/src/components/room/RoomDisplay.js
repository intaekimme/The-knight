import React from "react";
import { useSelector } from "react-redux";

import { Grid, Box } from "@mui/material";
import RoomUser from "./RoomUser";
import RoomHeader from "./RoomHeader";

export default function RoomDisplay(props) {
	const [size, setSize] = React.useState(props.size);
	React.useEffect(()=>{
		if(props.size){
			setSize(props.size);
		}
	}, [props.size]);

	// 방 정보
	const roomData = useSelector((state) => state.room.roomInfo);
	// 현재 대기방의 유저들 정보
	const memberDatas = useSelector(state => state.room.usersInfo);
	// websocket Client
	const stompClient = useSelector(state => state.websocket.stompClient);

  return (
		<Box sx={{display: "flex", flexDirection: "column", alignItems: "center", minWidth:size*35, height: size*23}}>
			<RoomHeader stompClient={stompClient} memberDatas={memberDatas} roomData={roomData} size={size}/>
			<Grid container>
				<Grid item xs={11}><RoomUser stompClient={stompClient} memberDatas={memberDatas} roomData={roomData} size={size}/></Grid>
				<Grid item xs={1}></Grid>
			</Grid>
		</Box>
  );
}
