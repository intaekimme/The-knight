import React, { useEffect } from 'react';

import { Modal, Box, Button, Grid, Input, InputLabel, MenuItem, FormControl, Select } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import ItemBox from "../ItemBox";

export default function RoomSetting(props) {
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

  const maxUserChange = (event) => {
    setMaxUser(event.target.value);
  };

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
					<Grid container item xs={12} sx={{mt:1, mb:1}}>
						<Grid item xs={2} sx={titleStyle}>아이템</Grid>
						<Grid item xs={2}><ItemBox text="검"/></Grid>
						<Grid item xs={2}><ItemBox text="쌍검"/></Grid>
						<Grid item xs={2}><ItemBox text="방패"/></Grid>
						<Grid item xs={2}><ItemBox text="맨손"/></Grid>
					</Grid>
				</Grid>
			</Box>
		</Modal>
	);
}