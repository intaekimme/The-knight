import React from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { Grid } from "@mui/material";
import RoomUser from "./RoomUser";
import RoomHeader from "./RoomHeader";

export default function RoomDisplay() {
	const size = 40;

	// 방 정보
	// const roomData = useSelector((state) => state.roomData.value);
	const roomData = {
		gameId: 1,
		title: "방제목",
		capacity: 1,
		participant: 1,
		sword: 1,
		twin: 1,
		shield: 1,
		hand: 1,
	};

	const currentUser = 5;
	const maxUser = 10;

	// 유저 목록
	// const userData = useSelector((state) => state.userData.value);
	const initUserDatas = [{
		nickname: "닉네임1",
		image: "url",
		ranking: 1,
		score: 100,
		win: 1,
		lose: 0,
	},
	{
		nickname: "닉네임2",
		image: "url",
		ranking: 1,
		score: 100,
		win: 1,
		lose: 0,
		},
		{
			nickname: "닉네임3",
			image: "url",
			ranking: 1,
			score: 100,
			win: 1,
			lose: 0,
		},
		{
			nickname: "닉네임4",
			image: "url",
			ranking: 1,
			score: 100,
			win: 1,
			lose: 0,
		},
	];
	const [userDatas, setUserDatas] = React.useState([]);
	React.useEffect(() => {
		setUserDatas([...initUserDatas]);
	}, [])
	const emptyUser = {
		nickname: "",
		image: "",
		ranking: -1,
		score: -1,
		win: -1,
		lose: -1,
	};

	React.useEffect(() => {
		const tempDatas = [...initUserDatas];
		while (tempDatas.length < maxUser) {
			tempDatas.push(emptyUser);
		}
		setUserDatas(tempDatas);
	}, [currentUser, maxUser]);

  return (
		<Grid container sx={{minWidth:size*35}}>
			<RoomHeader roomData={roomData} currentUser={currentUser} maxUser={maxUser} size={size}/>
			<RoomUser userDatas={userDatas} currentUser={currentUser} maxUser={maxUser} size={size}/>
		</Grid>
  );
}
