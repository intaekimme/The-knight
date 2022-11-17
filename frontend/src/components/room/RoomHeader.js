import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Grid, Box, Button } from "@mui/material";
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';

import api from "../../api/api";
import { white, red, blue, black } from "../../_css/ReactCSSProperties";
import {initChatting, addChatting} from '../../_slice/chattingSlice';
import RoomInfoModal from "../../commons/modal/RoomInfoModal";
import {onPubModifyRoom, onPubExit, onPubSelectTeam} from "../../websocket/RoomPublishes";

export default function RoomHeader(props) {
	const dispatch = useDispatch();

	// 방설정 모달
	const [open, setOpen] = React.useState(false);
	const onRoomInfoModalOpen = () => setOpen(true);
	const onRoomInfoModalClose = () => setOpen(false);

	// websocket client
	const stompClient = useSelector((state) => state.websocket.stompClient);
	// 방 설정변경
	const onRoomDataChange = (title, maxMember, itemCount)=>{
		const tempRoomData = {...roomData};
		tempRoomData.title = title;
		tempRoomData.maxMember = maxMember;
		tempRoomData.sword = itemCount[0];
		tempRoomData.twin = itemCount[1];
		tempRoomData.shield = itemCount[2];
		tempRoomData.hand = itemCount[3];
		let sum = 0;
    for(let i=0;i<itemCount.length;i++){
      sum += itemCount[i];
    }
    if(maxMember === sum){
			onPubModifyRoom({stompClient:stompClient, roomData:tempRoomData});
      onRoomInfoModalClose();
    }
    else{
      alert(`필요 아이템 개수 : ${maxMember} / 현재 아이템 개수 : ${sum}\n아이템 개수가 올바르지 않습니다`);
    }
	}
	// team A선택
	const onSelectTeamA = ()=>{
		dispatch(initChatting());
		onPubSelectTeam({stompClient:props.stompClient, gameId:roomData.gameId, team:'A'});
		stompClient.unsubscribe("chatTeam");
		stompClient.subscribe(api.subChatTeam(props.roomData.gameId, 'a')
			, (payload) => {
				// {
				//   memberId : long,
				//   nickname : String,
				//   content : String,
				//   chattingSet : String
				//    (ALL, A, B)
				// }
				const data = JSON.parse(payload.body);
				console.log("팀 채팅 sub", data);
				const text = `${data.nickname} : ${data.content}`;
				console.log(text);
				dispatch(addChatting(data));
			}
			, {id: "chatTeam"});
	}
	// team B선택
	const onSelectTeamB = ()=>{
		dispatch(initChatting());
		onPubSelectTeam({stompClient:props.stompClient, gameId:roomData.gameId, team:'B'});
		stompClient.unsubscribe("chatTeam");
		stompClient.subscribe(api.subChatTeam(props.roomData.gameId, 'b')
			, (payload) => {
				// {
				//   memberId : long,
				//   nickname : String,
				//   content : String,
				//   chattingSet : String
				//    (ALL, A, B)
				// }
				const data = JSON.parse(payload.body);
				console.log("팀 채팅 sub", data);
				const text = `${data.nickname} : ${data.content}`;
				console.log(text);
				dispatch(addChatting(data));
			}
			, {id: "chatTeam"});
	}
	// 나가기
	const onExit = () => {
		onPubExit({stompClient:props.stompClient, gameId:roomData.gameId});
	}

	// 방 정보
	const [roomData, setRoomData] = React.useState(props.roomData);
	React.useEffect(()=>{
		if(props.roomData){
			console.log(props.roomData);
			setRoomData(props.roomData);
		}
	}, [props.roomData]);
	
	return (
		<Grid container sx={{ p: 3 }}>
			<Grid item xs={7} sx={{fontSize:props.size, display: "flex", alignItems: "center"}}>
				{props.roomData.ownerId && props.roomData.ownerId.toString()===window.localStorage.getItem("memberId")
				?	<Button onClick={ onRoomInfoModalOpen }><SettingsIcon sx={{ color:"gray", fontSize: props.size*2 }} /></Button>
				: <div />
				}
				<RoomInfoModal canEdit={true} roomData={roomData} open={open} onClose={ onRoomInfoModalClose } onConfirm={onRoomDataChange}/>
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
				<Button onClick={onExit}><LogoutIcon sx={{ color:"red", fontSize: props.size*2 }} /></Button>
			</Grid>
		</Grid>
	);
}