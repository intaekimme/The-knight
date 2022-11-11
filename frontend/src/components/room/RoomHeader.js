import React from "react";
import { useSelector } from "react-redux";

import { white, red, blue, black } from "../../_css/ReactCSSProperties";

import { Grid, Box, Button, Modal } from "@mui/material";
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import RoomSetting from "../../commons/modal/RoomSetting";
import {onPubExitRoom, onPubSelectTeam} from "../../websocket/RoomPublishes";

export default function RoomHeader(props) {
	// 방설정 모달
	const [open, setOpen] = React.useState(false);
	const onRoomSettingOpen = () => setOpen(true);
	const onRoomSettingClose = () => setOpen(false);
	// team A선택
	const onSelectTeamA = ()=>{
		onPubSelectTeam({stompClient:props.stompClient, gameId:roomData.gameId, team:'A'});
	}
	// team A선택
	const onSelectTeamB = ()=>{
		onPubSelectTeam({stompClient:props.stompClient, gameId:roomData.gameId, team:'B'});
	}
	// 나가기
	const onExit = () => {
		onPubExitRoom({stompClient:props.stompClient, gameId:roomData.gameId});
	}

	// 방 정보
	const [roomData, setRoomData] = React.useState(props.roomData);
	React.useEffect(()=>{
		if(props.roomData){
			console.log(props.roomData);
			setRoomData(props.roomData);
		}
	}, [props.roomData]);

	const exit = () => {
		console.log("나가기");
	}
	return (
		<Grid container sx={{ p: 3 }}>
			<Grid item xs={7} sx={{fontSize:props.size, display: "flex", alignItems: "center"}}>
				<Button onClick={ onRoomSettingOpen } sx={{color:"gray"}}><SettingsIcon sx={{ fontSize: props.size*2 }} /></Button>
				<RoomSetting roomData={roomData} open={open} onClose={ onRoomSettingClose } />
				<h2>{` #${roomData.gameId} ${roomData.title} ${props.memberDatas.length}/${roomData.maxMember}`}</h2>
			</Grid>
			<Grid item xs={4} sx={{ fontSize: props.size*1.2, display: "flex", justifyContent: "center", alignItems: "center" }}>
				<Box sx={{width:props.size*10, height: props.size*4, border: "7px solid #4d4d4d", borderRadius: "10px", display: "flex", alignItems: "center", justifyContent: "center"}}>
					Team&nbsp;
					<Button sx={{ margin:0.5, color:white, fontSize: props.size, minWidth: props.size*2, width: props.size*2, height: props.size*2, background:red, border: `1px solid ${red}`, borderRadius: "50%" }}
						onClick={ onSelectTeamA }>A</Button>
					<Button sx={{ margin:0.5, color:white, fontSize: props.size, minWidth: props.size*2, width: props.size*2, height: props.size*2, background:blue, border: `1px solid ${blue}`, borderRadius: "50%" }}
						onClick={ onSelectTeamB }>B</Button>
				</Box>
			</Grid>
			<Grid item xs={1} sx={{display: "flex", justifyContent: "center", alignItems: "center"}}>
				<LogoutIcon onClick={exit} sx={{ color:"red", fontSize: props.size*2 }} />
			</Grid>
		</Grid>
	);
}