import React, { useEffect } from 'react';

import { Modal, Box, Button, Grid, Input, InputLabel, MenuItem, FormControl, Select } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import ArrowDropUpIcon from '@mui/icons-material/ArrowDropUp';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ItemBox from "../../commons/ItemBox";

const createRoom = () => {
  console.log("hi room");
}

export default function MakeRoomModal(props) {
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

	const inputStyle = {
		border: `none`,
		fontSize: 30
	};

	const [maxUser, setMaxUser] = React.useState('');
	const [itemCount, setItemCount] = React.useState([1,1,1,1]);

	const maxUserChange = (event) => {
    setMaxUser(event.target.value);
  };

	//아이템 목록
	const items = ["검", "쌍검", "방패", "맨손"];

	//아이템 개수증가
	const itemCountUp = (index)=>{
		const tempCount = [...itemCount];
		tempCount[index]++;
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

	return (
		<Modal
			open={props.open}
			// onClose={props.onClose}
			aria-labelledby="modal-modal-title"
			aria-describedby="modal-modal-description"
		>
			<Box sx={modalStyle}>
				<Box id="modal-modal-title" sx={{ textAlign: "right" }}>
					<Button onClick={ props.onClose } sx={{ color: "red" }}><ClearIcon /></Button>
				</Box>
				<Grid id="modal-modal-description" container>
					<Grid container item xs={12} sx={{mt:1, mb:1}}>
						<Grid item xs={2} sx={titleStyle}>방제목</Grid>
						<Grid item xs={10}><Input sx={{width:"100%"}}/></Grid>
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
										value={maxUser}
										label="인원 수"
										onChange={maxUserChange}
									>
										<MenuItem value={2}>2 vs 2</MenuItem>
										<MenuItem value={3}>3 vs 3</MenuItem>
										<MenuItem value={4}>4 vs 4</MenuItem>
										<MenuItem value={5}>5 vs 5</MenuItem>
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
				</Grid>
          <Grid sx={{display: 'flex', justifyContent: 'center' }}>
            <Button variant="outlined" onClick={createRoom}>방 만들기</Button>
          </Grid>
			</Box>
		</Modal>
	);
}