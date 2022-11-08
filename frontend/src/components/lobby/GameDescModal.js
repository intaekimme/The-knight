import React from 'react';

import { Modal, Box, Button, Grid, Card, CardActions, CardMedia, CardHeader, Typography, Paper,  } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import { useSelector, } from "react-redux";
import bgImg from "../../_assets/room/enterRoom.png"
import { borderLeft } from "@mui/system";

const enterRoom = () => {
	console.log("enter room");
}

export default function GameDescModal(props) {
	// const dispatch = useDispatch();
  // React.useEffect(() => {
	// 	if (props.open) {
	// 		console.log("props:", props.id);
  //     dispatch(gameDesc(props.id));
  //   }
	// }, []);
	
	const gameInfo = useSelector(state => state.tempGame.gameInfo)
	console.log(gameInfo);
	
	const modalStyle = {
		position: 'absolute',
		top: '50%',
		left: '50%',
		transform: 'translate(-50%, -50%)',
		width: '40vw',
		height: '27vw',
		// border: '1px solid #000',
		borderRadius: 2,
		boxShadow: '2px 2px 10px #000',
		p: '10px 36px 36px 36px',
		// bgImg: {bgImg},
		backgroundImage: `url(${bgImg})`,
		backgroundSize: 'cover'
	};
	// const modalBackStyle = {
	// 	top: 0,
  //   right: 0,
  //   height: "100%",
	// 	width: "100%",
	// 	opacity: 0.4
	// }
	const inModalStyle = {
		// top: '50%',
		// left: '50%',
		position: 'relative',
		pt: 1,
		pb: 1,
		width: '40vw',
		height: '21vw',
		bgcolor: '#424242',
	};
	// const cardmediaStyle = {
	// 	position: "absolute",
  //   top: 0,
  //   right: 0,
  //   height: "100%",
	// 	width: "100%",
	// }
	const titleStyle = {
		pr: 2,
		color: "#DCD7C9",
		fontWeight: 'bold',
		textAlign: "right",
		fontSize: 20
	};
	const infoStyle = {
		pl: 5,
		color: "#DCD7C9",
		fontSize: 18
	};
	const itemStyle = {
		color: "#DCD7C9",
		fontSize: 18
	};
	const gameInfoTitleStyle = {
		color: "#DCD7C9",
		fontSize: 25,
		fontWeight: 'bold',
	}
	const buttonStyle = {
		width: '90px',
		height: '40px',
		fontSize: 18,
		fontWeight: 'bold',
		color: '#424242',
		bgcolor: '#DCD7C9',
		border: '0px solid #424242',
		'&:hover':{
			color: '#fff',
			bgcolor: '#4F585B',
			border: '0px solid #DCD7C9',
		}
		
	}

	//아이템 목록
	// const items = ["검", "쌍검", "방패", "맨손"];

	return (
		<Modal
			open={props.open}
			// onClose={props.onClose}
			aria-labelledby="modal-modal-title"
			aria-describedby="modal-modal-description"
		>
			<Card sx={modalStyle}>
				{/* <CardMedia
					sx={cardmediaStyle}
					id="root"
          media="picture"
          alt="Contemplative Reptile"
					image={bgImg}
				></CardMedia> */}
				<Box id="modal-modal-title">
					<Grid container alignItems={'center'}>
						<Grid item xs={11} sx={{mt:1, mb:1}}>
							<Typography sx={gameInfoTitleStyle}>게임방 정보</Typography>
						</Grid>
						<Grid item xs={1}>
							<Button onClick={props.onClose} sx={{ color: "#DCD7C9" }}><ClearIcon /></Button>
						</Grid>
					</Grid>
				</Box>
				<Card sx={inModalStyle} variant="outlined" id="modal-modal-description" container>
					<Box sx={{
						mt: 2,
						height: '115px',
						display: 'flex',
						flexDirection: 'column',
					}}>
					<Grid container item xs={12}>
						<Grid item xs={2} sx={titleStyle}>방제목</Grid>
						<Grid item xs={10} sx={infoStyle}>{gameInfo.title}</Grid>
						{/* <Grid item xs={10}>title</Grid> */}
					</Grid>
					<Grid container item xs={12}>
						<Grid item xs={2} sx={titleStyle}>인원</Grid>
						<Grid item xs={10} sx={infoStyle}>{gameInfo.currentMembers}/{gameInfo.maxMember}</Grid>
						{/* <Grid item xs={10}>10/10</Grid> */}
					</Grid>
					<Grid container columns={24}>
						<Grid columns={24} item xs={4} sx={titleStyle} justifyContent="flex-end">아이템</Grid>
						</Grid>
						</Box>
					<Box sx={{
							mt:2,
							display: "flex",
						// flexWrap: "wrap",
							// "& > :not(style)": {
								// 	m: 0,
								// }
							justifyContent: "space-evenly"
						}}>
						<Box textAlign={"center"}>
							<Paper elevation={0} sx={{ width: 128, height: 128, mb: 1}} />
							<Typography sx={itemStyle}>{gameInfo.sword}</Typography>
						</Box>
						<Box textAlign={"center"}>
							<Paper elevation={0} sx={{ width: 128, height: 128, mb: 1}} />
							<Typography sx={itemStyle}>{gameInfo.twin}</Typography>
						</Box>
						<Box textAlign={"center"}>
							<Paper elevation={0} sx={{ width: 128, height: 128, mb: 1}} />
							<Typography sx={itemStyle}>{gameInfo.shield}</Typography>
						</Box>
						<Box textAlign={"center"}>
							<Paper elevation={0} sx={{ width: 128, height: 128, mb: 1}} />
							<Typography sx={itemStyle}>{gameInfo.hand}</Typography>
						</Box>
						</Box>
				</Card>
				<CardActions sx={{display: 'flex', justifyContent: 'center', pt:1 }}>
            <Button variant="outlined" onClick={enterRoom} sx={buttonStyle}>입장</Button>
				</CardActions>
			</Card>
		</Modal>
	);
}