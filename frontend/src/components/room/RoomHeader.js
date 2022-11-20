import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Grid, Box, Button } from "@mui/material";
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';

import api from "../../api/api";
import { white, red, blue, black } from "../../_css/ReactCSSProperties";
import { leftInfoGrid, settingIcon, rightInfoGrid, selectTeamBox, selectTeamButton, exitGrid, exitIcon } from "../../_css/RoomHeaderCSSProperties";
import { initChatting, addChatting } from '../../_slice/chattingSlice';
import RoomInfoModal from "../../commons/modal/RoomInfoModal";
import { onPubModifyRoom, onPubExit, onPubSelectTeam } from "../../websocket/RoomPublishes";

export default function RoomHeader(props) {
	const dispatch = useDispatch();

	// 방설정 모달
	const [open, setOpen] = React.useState(false);
	const onRoomInfoModalOpen = () => setOpen(true);
	const onRoomInfoModalClose = () => setOpen(false);

	// websocket client
	const stompClient = useSelector((state) => state.websocket.stompClient);
	// 방 설정변경
	const onRoomDataChange = (title, maxMember, itemCount) => {
		const tempRoomData = { ...roomData };
		tempRoomData.title = title;
		tempRoomData.maxMember = maxMember;
		tempRoomData.sword = itemCount[0];
		tempRoomData.twin = itemCount[1];
		tempRoomData.shield = itemCount[2];
		tempRoomData.hand = itemCount[3];
		let sum = 0;
		for (let i = 0; i < itemCount.length; i++) {
			sum += itemCount[i];
		}
		if (maxMember === sum) {
			onPubModifyRoom({ stompClient: stompClient, roomData: tempRoomData });
			onRoomInfoModalClose();
		}
		else {
			alert(`필요 아이템 개수 : ${maxMember} / 현재 아이템 개수 : ${sum}\n아이템 개수가 올바르지 않습니다`);
		}
	}
	// team A선택
	const onSelectTeamA = () => {
		dispatch(initChatting());
		onPubSelectTeam({ stompClient: props.stompClient, gameId: roomData.gameId, team: 'A' });
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
			, { id: "chatTeam" });
	}
	// team B선택
	const onSelectTeamB = () => {
		dispatch(initChatting());
		onPubSelectTeam({ stompClient: props.stompClient, gameId: roomData.gameId, team: 'B' });
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
			, { id: "chatTeam" });
	}
	// 나가기
	const onExit = () => {
		onPubExit({ stompClient: props.stompClient, gameId: roomData.gameId });
	}

	// 방 정보
	const [roomData, setRoomData] = React.useState(props.roomData);
	React.useEffect(() => {
		if (props.roomData) {
			console.log(props.roomData);
			setRoomData(props.roomData);
		}
	}, [props.roomData]);

	return (
		<Grid container sx={{ p: 0.5, pt: 3 }}>
			<Grid item xs={7} sx={{ ...leftInfoGrid(props.size), pl: 3 }}>
				{props.roomData.ownerId && props.roomData.ownerId.toString() === window.localStorage.getItem("memberId")
					? <Button onClick={onRoomInfoModalOpen}><SettingsIcon sx={{ ...settingIcon(props.size) }} /></Button>
					: <div />
				}
				<RoomInfoModal canEdit={true} roomData={roomData} open={open} onClose={onRoomInfoModalClose} onConfirm={onRoomDataChange} />
				{/* <h5 style={{ marginLeft: 30 }}>{` #${roomData.gameId} ${roomData.title} ${props.memberDatas.length}/${roomData.maxMember}`}</h5> */}
				<h4 style={{ marginLeft: 30 }}>#{roomData.gameId}&nbsp;&nbsp; <span style={{ color: '#DCD7C9', fontSize: 30 }}>{roomData.title}</span> <span style={{ color: '#DCD7C9', fontSize: 18 }}>({props.memberDatas.length}/{roomData.maxMember})</span></h4>
			</Grid>
			<Grid item xs={4} sx={{ ...rightInfoGrid(props.size) }}>
				<Box sx={{ ...selectTeamBox(props.size) }}>
					Team&nbsp;
					<Button sx={{ ...selectTeamButton(props.size), background: '#D2AB66', border: `1px solid ${red}` }}
						onClick={onSelectTeamA}>A</Button>
					<Button sx={{ ...selectTeamButton(props.size), background: '#6A6969', border: `1px solid ${blue}` }}
						onClick={onSelectTeamB}>B</Button>
				</Box>
			</Grid>
			<Grid item xs={1} sx={{ ...exitGrid(props.size) }}>
				<Button onClick={onExit}><LogoutIcon sx={{ ...exitIcon(props.size) }} /></Button>
			</Grid>
		</Grid>
	);
}