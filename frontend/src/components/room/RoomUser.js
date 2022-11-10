import React, { useEffect } from "react";

import { Grid, Box } from "@mui/material";
import UserBox from "../../commons/user/UserBox";

export default function RoomUser(props) {
	const emptyUser = {
		nickname: "",
		image: "",
		ranking: -1,
		score: -1,
		win: -1,
		lose: -1,
	};
	const [userDatas, setUserDatas] = React.useState([]);
	React.useEffect(()=>{
		const tempDatas = [...props.userDatas];
		while(tempDatas.length < props.maxMember){
			tempDatas.push(emptyUser);
		}
		console.log(tempDatas);
		setUserDatas(tempDatas);
	}, [props.userDatas]);
	return (
		<Box sx={{p: 3, display: "flex", justifyContent: "space-evenly",flexDirection: "column", width: "70%", height: "100%"}}>
			<Box sx={{display: "flex", justifyContent: "space-evenly"}}>
				{userDatas.slice(0, parseInt(props.maxMember / 2)).map((userData, index) => (
					<UserBox key={`user${userData.nickname}${index + parseInt(props.maxMember / 2) - parseInt(props.maxMember / 2)}`}
						userData={userData} width={props.size * 6} height={props.size * 6}/>
				))}
			</Box>
			<Box sx={{ display: "flex", justifyContent: "space-evenly"}}>
				{userDatas.slice(parseInt(props.maxMember / 2), userDatas.length).map((userData, index) => (
					<UserBox key={`user${userData.nickname}${index + parseInt(props.maxMember / 2)}`} 
					userData={userData} width={props.size*6} height={props.size*6}/>
				))}
			</Box>
		</Box>
	);
}