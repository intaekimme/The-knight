import React, { useEffect } from "react";

import { Grid, Box } from "@mui/material";
import UserBox from "../../commons/user/UserBox";

export default function RoomUser(props) {
	const emptyUser = {
    id: -1,
    nickname: "",
    image: "",
    readyStatus: false,
    team: 'B',
    ranking: -1,
    score: -1,
    win: -1,
    lose: -1,
    empty: true,
	};
	// users 정보
	const [memberDatas, setMemberDatas] = React.useState([]);
	React.useEffect(()=>{
		console.log(props.memberDatas);
		const tempDatas = [...props.memberDatas];
		while(tempDatas.length < props.roomData.maxMember){
			tempDatas.push(emptyUser);
		}
		console.log(tempDatas);
		setMemberDatas(tempDatas);
	}, [props.memberDatas, props.roomData]);
	
	// 방 정보
	const [roomData, setRoomData] = React.useState(props.roomData);
	React.useEffect(()=>{
		if(props.roomData){
			console.log(props.roomData);
			setRoomData(props.roomData);
		}
	}, [props.roomData]);
	return (
		<Box sx={{p: 3, display: "flex", justifyContent: "space-evenly",flexDirection: "column", width: "70%", height: "100%"}}>
			<Box sx={{display: "flex", justifyContent: "space-evenly"}}>
				{memberDatas.slice(0, parseInt(roomData.maxMember / 2)).map((memberData, index) => (
					<UserBox key={`user${memberData.nickname}${index + parseInt(roomData.maxMember / 2) - parseInt(roomData.maxMember / 2)}`}
						userData={memberData} width={props.size * 6} height={props.size * 6}/>
				))}
			</Box>
			<Box sx={{ display: "flex", justifyContent: "space-evenly"}}>
				{memberDatas.slice(parseInt(roomData.maxMember / 2), memberDatas.length).map((memberData, index) => (
					<UserBox key={`user${memberData.nickname}${index + parseInt(roomData.maxMember / 2)}`} 
					userData={memberData} width={props.size*6} height={props.size*6}/>
				))}
			</Box>
		</Box>
	);
}