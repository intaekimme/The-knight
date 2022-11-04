import React from "react";

import { Grid } from "@mui/material";
import UserBox from "../../commons/user/UserBox";

export default function RoomUser(props) {
	console.log(props.userDatas.slice(0, parseInt(props.maxUser / 2)));
	console.log(props.userDatas.slice(parseInt(props.maxUser / 2), props.userDatas.length));
	return (
		<Grid container item xs={12} sx={{p:10}}>
			<Grid container justifyContent="center" item xs={12}>
				{props.userDatas.slice(0, parseInt(props.maxUser / 2)).map((userData, index) => (
					<UserBox key={`user${userData.nickname}${index + parseInt(props.maxUser / 2) - parseInt(props.maxUser / 2)}`}
					userData={userData} width={props.size*6} height={props.size*6} />
				))}
			</Grid>
			<Grid container justifyContent="center" item xs={12}>
				{props.userDatas.slice(parseInt(props.maxUser / 2), props.userDatas.length).map((userData, index) => (
					<UserBox key={`user${userData.nickname}${index + parseInt(props.maxUser / 2)}`} 
					userData={userData} width={props.size*6} height={props.size*6} />
				))}
			</Grid>
		</Grid>
	);
}