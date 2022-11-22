import React from 'react';
import { useSelector } from 'react-redux';
import { Modal, Box, Button, Grid, Input, InputLabel, MenuItem, FormControl, Select } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import ArrowDropUpIcon from '@mui/icons-material/ArrowDropUp';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import modalBackground from "../../_assets/room/roomInfo.png";
import swordIcon from "../../_assets/game/image/sword-icon.png";
import twinSwordIcon from "../../_assets/game/image/twin-icon.png";
import shieldIcon from "../../_assets/game/image/shield-icon.png";
import handIcon from "../../_assets/game/image/hand-icon.png";
import ItemBox from "../ItemBox";
import { red, blue, white, black, backgroundWhite } from '../../_css/ReactCSSProperties';
import { modalStyle, inModalStyle, titleStyle, infoStyle, itemStyle, roomInfoTitleStyle, buttonStyle } from '../../_css/ModalCSSProperties';

// props - canEdit:boolean, roomData:redux, open:boolean_modalOpen, onClose:function_modalClose, onConfirm:function
export default function RoomSetting(props) {
	// websocket client
	const stompClient = useSelector((state) => state.websocket.stompClient);
	// 편집 가능 여부
	const [canEdit, setCanEdit] = React.useState(false);
	// 방 정보
	const [roomData, setRoomData] = React.useState(props.roomData);
	// 방제목
	const [title, setTitle] = React.useState(props.roomData.title);
	// 최대 유저수
	const [maxMember, setMaxMember] = React.useState(props.roomData.maxMember);
	// item count
	const [itemCount, setItemCount] = React.useState([props.roomData.sword, props.roomData.twin, props.roomData.shield, props.roomData.hand]);

	// roomData update
	React.useEffect(() => {
		console.log(props.roomData);
		if (props.roomData) {
			setRoomData(props.roomData);
			setItemCount([props.roomData.sword, props.roomData.twin, props.roomData.shield, props.roomData.hand]);
		}
	}, [props.roomData]);

	// 편집가능여부 판단
	React.useEffect(() => {
		if (props.canEdit) {
			setCanEdit(props.canEdit);
		}
	}, [props.canEdit]);

	// 설정변경 취소
	const onModalClose = () => {
		setTitle(roomData.title);
		setMaxMember(roomData.maxMember);
		setItemCount([roomData.sword, roomData.twin, roomData.shield, roomData.hand]);
		props.onClose();
	}

	// 확인 버튼
	const onConfirm = () => {
		props.onConfirm(title, maxMember, itemCount);
	}

	// 인원변경 시 아이템개수 초기화
	const maxMemberChange = (event) => {
		const maxMemberChange = event.target.value;
		setMaxMember(maxMemberChange);
		switch (maxMemberChange) {
			case 4:
				setItemCount([1, 1, 1, 1]);
				break;
			case 6:
				setItemCount([2, 1, 2, 1]);
				break;
			case 8:
				setItemCount([2, 2, 2, 2]);
				break;
			case 10:
				setItemCount([3, 2, 3, 2]);
				break;
			default:
				alert('인원 수 오류');
				break;
		}
	};

	//아이템 목록
	const items = ["검", "쌍검", "방패", "맨손"];
	// item img
	const itemImages = [swordIcon, twinSwordIcon, shieldIcon, handIcon];

	//아이템 개수증가
	const itemCountUp = (index) => {
		const tempCount = [...itemCount];
		let tempSum = 0;
		tempCount.forEach(count => { tempSum += count; });
		console.log(tempSum);
		if (tempSum < maxMember) {
			tempCount[index]++;
		}
		else {
			alert("현재 무기 개수의 합이 최대입니다.");
		}
		setItemCount(tempCount);
	}

	// 아이템 개수감소
	const itemCountDown = (index) => {
		const tempCount = [...itemCount];
		if (tempCount[index] > 0) {
			tempCount[index]--;
		}
		setItemCount(tempCount);
	}

	const onChangeTitle = (e) => {
		setTitle(e.target.value);
	}

	return (
		<Modal
			open={props.open}
			// onClose={props.onClose}
			aria-labelledby="modal-modal-title"
			aria-describedby="modal-modal-description"
			sx={{ background: 'rgba(0, 0, 0, 0.5)' }}
		>
			<Box sx={{ ...modalStyle, backgroundImage: `url(${modalBackground})`, backgroundSize: 'cover' }}>
				<Box id="modal-modal-title">
					<Grid container alignItems={'center'} sx={{ p: 0, pt: 1, pb: 2 }}>
						<Grid item xs={11} sx={roomInfoTitleStyle}>게임방 정보</Grid>
						<Grid item xs={1}>
							<Button onClick={onModalClose} sx={{ color: "#DCD7C9" }}><ClearIcon /></Button>
						</Grid>
					</Grid>
				</Box>
				<Grid id="modal-modal-description" container sx={inModalStyle}>
					<Box sx={{ mt: 2, display: 'flex', flexDirection: 'column' }}>
						<Grid container item xs={12} sx={{ mt: 1, mb: 1 }}>
							<Grid item xs={2} sx={titleStyle}>방제목</Grid>
							{canEdit ?
								<Grid item xs={10} sx={{ pl: 4, pr: 4 }}><Input sx={{ ...infoStyle, color: black, background: backgroundWhite, pl: 3, width: "100%"}} defaultValue={title} onChange={onChangeTitle} /></Grid>
								:
								<Grid item xs={10} sx={{ ...infoStyle, pl: 4, pr: 4 }}>{roomData.title}</Grid>
							}
						</Grid>
						<Grid container item xs={12} sx={{ mt: 1, mb: 1 }}>
							<Grid item xs={2} sx={titleStyle}>인원</Grid>
							{canEdit ?
								<Grid item xs={10} sx={{ pl: 4, pr: 4 }}>
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
												sx={{ ...infoStyle, color: black, background: backgroundWhite, width: "100%"}}
											>
												<MenuItem value={4} sx={{ fontSize: 18 }}>2 vs 2</MenuItem>
												<MenuItem value={6} sx={{ fontSize: 18 }}>3 vs 3</MenuItem>
												<MenuItem value={8} sx={{ fontSize: 18 }}>4 vs 4</MenuItem>
												<MenuItem value={10} sx={{ fontSize: 18 }}>5 vs 5</MenuItem>
											</Select>
										</FormControl>
									</Box>
								</Grid>
								:
								<Grid item xs={10} sx={{ ...infoStyle, pl: 4, pr: 4 }}>{roomData.currentMembers}/{roomData.maxMember}</Grid>
							}
						</Grid>
						<Grid container columns={24} item xs={24} sx={{ mt: 1, mb: 1 }}>
							<Grid container columns={24} item xs={4} sx={titleStyle} justifyContent="flex-end">아이템</Grid>
							{items.map((item, index) => (
								<Grid container columns={24} item xs={5} key={`item${item}`} sx={{textAlign:"center"}}>
									<Grid columns={24} item xs={24} sx={{textAlign:"center"}}><ItemBox textColor="#DCD7C9" buttonDisabled={true} image={ itemImages[index] } /></Grid>
									{/* <Grid columns={24} item xs={24}><ItemBox text={item} size={190} textColor="#DCD7C9" buttonDisabled={true}/></Grid> */}
									<Grid columns={24} container item xs={24} alignItems="center">
										<Grid columns={24} item xs={14} sx={itemStyle}>{itemCount[index]}</Grid>
										{canEdit ?
											<Grid container columns={24} item xs={10} sx={{ mt: '-15px' }}>
												<Grid columns={24} item xs={24} alignItems="center">
													<Button onClick={(e) => { itemCountUp(index); }} sx={{ width: "100%", height: "100%", color: "#DCD7C9" }}><ArrowDropUpIcon /></Button>
												</Grid>
												<Grid columns={24} item xs={24} alignItems="center">
													<Button onClick={(e) => { itemCountDown(index); }} sx={{ width: "100%", height: "100%", color: "#DCD7C9" }}><ArrowDropDownIcon /></Button>
												</Grid>
											</Grid>
											:
											<div />
										}
									</Grid>
								</Grid>
							))}
						</Grid>
					</Box>
				</Grid>
				{canEdit ?
					<Grid container sx={{ mt: 4 }}>
						<Grid item xs={6} alignItems="center" sx={{ textAlign: "center" }}><Button variant="outlined" sx={buttonStyle} onClick={onConfirm}>확인</Button></Grid>
						<Grid item xs={6} alignItems="center" sx={{ textAlign: "center" }}><Button variant="outlined" sx={buttonStyle} onClick={onModalClose}>취소</Button></Grid>

						{/* <Grid item xs={6} alignItems="center" sx={{textAlign:"center"}}><Button variant="outlined" sx={{...buttonStyle, background: blue, color: "#DCD7C9"}} onClick={onRoomInfoChange}>확인</Button></Grid>
					<Grid item xs={6} alignItems="center" sx={{textAlign:"center"}}><Button variant="outlined" sx={{...buttonStyle, background: red, color: "#DCD7C9"}} onClick={onModalClose}>취소</Button></Grid> */}
					</Grid>
					:
					<Grid item xs={12} alignItems="center" sx={{ mt: 4, textAlign: "center" }}><Button variant="outlined" sx={buttonStyle} onClick={onConfirm}>입장</Button></Grid>
					// <Grid container sx={{mt:5}}><Button variant="outlined" onClick={enterRoom} sx={buttonStyle}>입장</Button></Grid>
				}
			</Box>
		</Modal>
	);
}