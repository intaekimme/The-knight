import React from "react";
import { useSelector } from "react-redux";

import { Grid, Box } from "@mui/material";
import RoomUser from "./RoomUser";
import RoomHeader from "./RoomHeader";

export default function RoomDisplay() {
	const size = 35;

	// 방 정보
<<<<<<< frontend/src/components/room/RoomDisplay.js
	// const roomData = useSelector((state) => state.roomData.value);
	const roomData = {
		gameId: 1,
		title: "방제목",
		capacity: 1,
		participant: 1,
		sword: 1,
		twin: 1,
		shield: 1,
		hand: 1,
	};

	const currentUser = 5;
	const maxMember = 10;
	
	const userDatas = useSelector(state=>state.room.usersInfo);
	const emptyUser = {
		nickname: "",
		image: "",
		ranking: -1,
		score: -1,
		win: -1,
		lose: -1,
	};

  return (
		<Box sx={{display: "flex", flexDirection: "column", alignItems: "center", minWidth:size*35, height: size*23}}>
			<RoomHeader roomData={roomData} currentUser={currentUser} maxMember={maxMember} size={size}/>
			<RoomUser userDatas={userDatas} currentUser={currentUser} maxMember={maxMember} size={size}/>
=======
	const roomData = useSelector((state) => state.room.roomInfo);
	// 현재 대기방의 유저들 정보
	const memberDatas = useSelector(state => state.room.usersInfo);
	// websocket Client
	const stompClient = useSelector(state => state.websocket.stompClient);

  return (
		<Box sx={{display: "flex", flexDirection: "column", alignItems: "center", minWidth:size*35, height: size*23}}>
			<RoomHeader stompClient={stompClient} memberDatas={memberDatas} roomData={roomData} size={size}/>
			<RoomUser stompClient={stompClient} memberDatas={memberDatas} roomData={roomData} size={size}/>
>>>>>>> frontend/src/components/room/RoomDisplay.js
		</Box>
  );
}
