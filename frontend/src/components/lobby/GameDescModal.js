import React from 'react';

import { Modal, Box, Button, Grid } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import ItemBox from "../../commons/ItemBox";
import { useSelector, useDispatch } from "react-redux";
import { gameDesc } from "../../_slice/tempGameSlice";

export default function GameDescModal(props) {
	const dispatch = useDispatch();
  React.useEffect(() => {
		if (props.open) {
			console.log("props:", props.id);
      dispatch(gameDesc(props.id));
    }
	}, []);
	
	const gameInfo = useSelector(state => state.tempGame.gameInfo)
	console.log(gameInfo);
	
	const modalStyle = {
		position: 'absolute',
		top: '50%',
		left: '50%',
		transform: 'translate(-50%, -50%)',
		width: '50vw',
		bgcolor: 'background.paper',
		border: '1px solid #000',
		boxShadow: 24,
		p: 4,
	};

	const titleStyle = {
		pr: 2,
		textAlign: "right",
		fontSize: 30
	};

	//아이템 목록
	const items = ["검", "쌍검", "방패", "맨손"];

	return (
		<Modal
			open={props.open}
			// onClose={props.onClose}
			aria-labelledby="modal-modal-title"
			aria-describedby="modal-modal-description"
		>
			<Box sx={modalStyle}>
				<Box id="modal-modal-title" sx={{ textAlign: "right" }}>
					<Button onClick={props.onClose} sx={{ color: "red" }}><ClearIcon /></Button>
				</Box>
				<Grid id="modal-modal-description" container>
					<Grid container item xs={12} sx={{ mt: 1, mb: 1 }}>
						<Grid item xs={2} sx={titleStyle}>방제목</Grid>
						{/* <Grid item xs={10}>{gameInfo.title}</Grid> */}
						<Grid item xs={10}>title</Grid>
					</Grid>
					<Grid container item xs={12} sx={{ mt: 1, mb: 1 }}>
						<Grid item xs={2} sx={titleStyle}>인원</Grid>
						{/* <Grid item xs={10}>{gameInfo.currentUser}/{gameInfo.maxUser}</Grid> */}
						<Grid item xs={10}>10/10</Grid>
					</Grid>
					<Grid container columns={24} item xs={24} sx={{ mt: 1, mb: 1 }}>
						<Grid container columns={24} item xs={4} sx={titleStyle} justifyContent="flex-end">아이템</Grid>
						{items.map((item, index) => (
							<Grid container columns={24} item xs={5} key={`item${item}`}>
								<Grid columns={12} item xs={12}><ItemBox text={item} size={190} /></Grid>
								<Grid columns={12} container item xs={12} alignItems="center">
									<Grid columns={12} item xs={7} sx={{ pr: 2, textAlign: "right", fontSize: 30 }}>4</Grid>
								</Grid>
							</Grid>
						))}
					</Grid>
				</Grid>
			</Box>
		</Modal>
	);
}