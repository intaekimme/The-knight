import React from "react";

import { white, red, blue, black } from "../../_css/ReactCSSProperties";

import { Grid, Box, Button, Modal } from "@mui/material";
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import RoomSetting from "../../commons/modal/RoomSetting";

export default function RoomHeader(props) {
	// 방설정 모달
	const [open, setOpen] = React.useState(false);
	const roomSettingOpen = () => setOpen(true);
	const roomSettingClose = () => setOpen(false);

	const exit = () => {
		console.log("나가기");
	}

	return (
		<Grid container item xs={12} sx={{p:10}}>
			<Grid item xs={7} alignItems="center" sx={{fontSize:props.size}}>
				<Button onClick={roomSettingOpen} sx={{color:"gray"}}><SettingsIcon sx={{ fontSize: props.size*2 }} /></Button>
				<RoomSetting open={open} onClose={ roomSettingClose } />
				{` #${props.roomData.gameId} ${props.roomData.title} ${props.currentUser}/${props.maxUser}`}
			</Grid>
			<Grid item xs={4} alignItems="center" sx={{ fontSize: props.size*1.2 }}>
				<Box sx={{width:props.size*8, border: "1px solid black"}}>
					Team&nbsp;
					<Button sx={{ margin:0.5, color:white, fontSize: props.size, width: props.size*2, height: props.size*2, background:red, border: `1px solid ${red}`, borderRadius: "50%" }}>A</Button>
					<Button sx={{ margin:0.5, color:white, fontSize: props.size, width: props.size*2, height: props.size*2, background:blue, border: `1px solid ${blue}`, borderRadius: "50%" }}>B</Button>
				</Box>
			</Grid>
			<Grid item xs={1} alignItems="center" sx={{textAlign:"right"}}>
				<Button onClick={exit} sx={{color:"red"}}><LogoutIcon sx={{ fontSize: props.size*2 }} /></Button>
			</Grid>
		</Grid>
	);
}