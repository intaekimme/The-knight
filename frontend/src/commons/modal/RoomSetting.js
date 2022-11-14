import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { Modal, Box, Button, Grid, Input, InputLabel, MenuItem, FormControl, Select } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import ArrowDropUpIcon from '@mui/icons-material/ArrowDropUp';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ItemBox from "../ItemBox";
import { red, blue } from '../../_css/ReactCSSProperties';
import {onPubModifyRoom} from "../../websocket/RoomPublishes";
import { modifyRoomSetting } from '../../_slice/roomSlice';

export default function RoomSetting(props) {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const modalStyle = {
		position: 'absolute',
		top: '50%',
		left: '50%',
		transform: 'translate(-50%, -50%)',
		width: 1000,
		bgcolor: 'background.paper',
		border: '2px solid #000',
		boxShadow: 24,
		p: 4,
	};

	const titleStyle = {
		pr: 2,
		textAlign: "right",
		fontSize: 30
	};

	const buttonStyle = {
		border: `none`,
		fontSize: 30
	};

	// websocket client
	const stompClient = useSelector((state) => state.websocket.stompClient);
	// 방 정보
	const [roomData, setRoomData] = React.useState(props.roomData);
	// 방제목
	const [title, setTitle] = React.useState(props.roomData.title);
	// 최대 유저수
	const [maxMember, setMaxMember] = React.useState(props.roomData.maxMember);
	// item count
	const [itemCount, setItemCount] = React.useState([props.roomData.sword, props.roomData.twin, props.roomData.shield, props.roomData.hand]);

	React.useEffect(()=>{
		if(props.roomData){
			console.log(props.roomData);
			setRoomData(props.roomData);
		}
	}, [props.roomData]);

	// 설정변경 취소
	const onModalClose = ()=>{
		setTitle(roomData.title);
		setMaxMember(roomData.maxMember);
		setItemCount([roomData.sword, roomData.twin, roomData.shield, roomData.hand]);
		props.onClose();
	}
	// 설정변경 확정
	const onChangeSetting = ()=>{
		const tempRoomData = {navigate:navigate, url:"/makeroom", ...roomData};
		tempRoomData.title = title;
		tempRoomData.maxMember = maxMember;
		tempRoomData.sword = itemCount[0];
		tempRoomData.twin = itemCount[1];
		tempRoomData.shield = itemCount[2];
		tempRoomData.hand = itemCount[3];
		if(tempRoomData.gameId!=-1){
			onPubModifyRoom({stompClient:stompClient, roomInfo:tempRoomData});
		}
		else{
			dispatch(modifyRoomSetting(tempRoomData));
		}
		props.onClose();
	}
	const maxMemberChange = (event) => {
    setMaxMember(event.target.value);
  };

	//아이템 목록
	const items = ["검", "쌍검", "방패", "맨손"];

	//아이템 개수증가
	const itemCountUp = (index)=>{
		const tempCount = [...itemCount];
		let tempSum = 0;
		tempCount.forEach(count=>{ tempSum+=count; });
		console.log(tempSum);
		if(tempSum<maxMember){
			tempCount[index]++;
		}
		else{
			alert("현재 무기 개수의 합이 최대입니다.");
		}
		setItemCount(tempCount);
	}

	// 아이템 개수감소
	const itemCountDown = (index)=>{
		const tempCount = [...itemCount];
		if(tempCount[index]>0){
			tempCount[index]--;
		}
		setItemCount(tempCount);
	}

	const onChangeTitle = (e)=>{
		setTitle(e.target.value);
	}

	return (
		<Modal
			open={props.open}
			// onClose={props.onClose}
			aria-labelledby="modal-modal-title"
			aria-describedby="modal-modal-description"
		>
			<Box sx={modalStyle}>
				<Box id="modal-modal-title" sx={{ textAlign: "right" }}>
					<Button onClick={ onModalClose } sx={{ color: "red" }}><ClearIcon /></Button>
				</Box>
				<Grid id="modal-modal-description" container sx={{mt:4}}>
					<Grid container item xs={12} sx={{mt:1, mb:1}}>
						<Grid item xs={2} sx={titleStyle}>방제목</Grid>
						<Grid item xs={10}><Input sx={{width:"100%"}} defaultValue={title} onChange={onChangeTitle}/></Grid>
					</Grid>
					<Grid container item xs={12} sx={{mt:1, mb:1}}>
						<Grid item xs={2} sx={titleStyle}>인원</Grid>
						<Grid item xs={10}>
							<Box sx={{ minWidth: 120 }}>
								<FormControl fullWidth>
									<InputLabel id="select-label">인원 수</InputLabel>
									<Select
										labelId="select-label"
										id="select"
										value={maxMember}
										defaultValue={maxMember}
										label="인원 수"
										onChange={maxMemberChange}
									>
										<MenuItem value={4}>2 vs 2</MenuItem>
										<MenuItem value={6}>3 vs 3</MenuItem>
										<MenuItem value={8}>4 vs 4</MenuItem>
										<MenuItem value={10}>5 vs 5</MenuItem>
									</Select>
								</FormControl>
							</Box>
						</Grid>
					</Grid>
					<Grid container columns={24} item xs={24} sx={{mt:1, mb:1}}>
						<Grid container columns={24} item xs={4} sx={titleStyle} justifyContent="flex-end">아이템</Grid>
						{items.map((item, index)=>(
							<Grid container columns={24} item xs={5} key={`item${item}`}>
								<Grid columns={12} item xs={12}><ItemBox text={item} size={190}/></Grid>
								<Grid columns={12} container item xs={12} alignItems="center">
									<Grid columns={12} item xs={7} sx={{pr:2, textAlign:"right", fontSize:30}}>{itemCount[index]}</Grid>
									<Grid container columns={12} item xs={5}>
										<Grid columns={12} item xs={12} alignItems="center">
											<Button onClick={(e)=>{itemCountUp(index);}} sx={{width: "100%", height:"100%"}}><ArrowDropUpIcon/></Button>
										</Grid>
										<Grid columns={12} item xs={12} alignItems="center">
											<Button onClick={(e)=>{itemCountDown(index);}} sx={{width: "100%", height:"100%"}}><ArrowDropDownIcon/></Button>
										</Grid>
									</Grid>
								</Grid>
							</Grid>
						))}
					</Grid>
					<Grid item xs={6} alignItems="center" sx={{textAlign:"center"}}><Button sx={{...buttonStyle, background: blue}} onClick={onChangeSetting}>확인</Button></Grid>
					<Grid item xs={6} alignItems="center" sx={{textAlign:"center"}}><Button sx={{...buttonStyle, background: red}} onClick={onModalClose}>취소</Button></Grid>
				</Grid>
			</Box>
		</Modal>
	);
}