import React from "react";

import { Grid, Box } from "@mui/material";
import UserBox from "../../commons/user/UserBox";

export default function RoomUser(props) {
	// console.log(props.userDatas.slice(0, parseInt(props.maxUser / 2)));
	// console.log(props.userDatas.slice(parseInt(props.maxUser / 2), props.userDatas.length));
	return (
		<Box sx={{p: 3, display: "flex", justifyContent: "space-evenly",flexDirection: "column", width: "70%", height: "100%"}}>
			<Box sx={{display: "flex", justifyContent: "space-evenly"}}>
				{props.userDatas.slice(0, parseInt(props.maxUser / 2)).map((userData, index) => (
					<UserBox key={`user${userData.nickname}${index + parseInt(props.maxUser / 2) - parseInt(props.maxUser / 2)}`}
						userData={userData} width={props.size * 6} height={props.size * 6}/>
				))}
			</Box>
			<Box sx={{ display: "flex", justifyContent: "space-evenly"}}>
				{props.userDatas.slice(parseInt(props.maxUser / 2), props.userDatas.length).map((userData, index) => (
					<UserBox key={`user${userData.nickname}${index + parseInt(props.maxUser / 2)}`} 
					userData={userData} width={props.size*6} height={props.size*6}/>
				))}
			</Box>
		</Box>
	);
}