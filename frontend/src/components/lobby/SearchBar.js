import React from "react";
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from "react-redux";

import { Button, Grid } from "@mui/material";
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';

import api from "../../api/api";

import { searchRoom } from "../../_slice/tempGameSlice";
import { modifyRoomSetting, roomInit, initRoom } from "../../_slice/roomSlice";

import RoomInfoModal from "../../commons/modal/RoomInfoModal";

export default function SearchBar() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  // 방설정 모달
  const [open, setOpen] = React.useState(false);
  const onRoomInfoModalOpen = () => setOpen(true);
  const onRoomInfoModalClose = () => setOpen(false);

  // 방 정보
  const roomData = useSelector((state) => state.room.roomInfo);

  // 방만들기 정보 update
  const onRoomMake = (title, maxMember, itemCount) => {
    const tempRoomData = { ...roomData };
    tempRoomData.title = title;
    tempRoomData.maxMember = maxMember;
    tempRoomData.sword = itemCount[0];
    tempRoomData.twin = itemCount[1];
    tempRoomData.shield = itemCount[2];
    tempRoomData.hand = itemCount[3];
    let sum = 0;
    for (let i = 0; i < itemCount.length; i++) {
      sum += itemCount[i];
    }
    if (maxMember === sum) {
      dispatch(modifyRoomSetting(tempRoomData));
      dispatch(initRoom({ roomInfo: tempRoomData })).then((response) => {
        const gameId = response.payload.gameId;
        navigate(api.routeConnectWebsocket(gameId));
      });
      onRoomInfoModalClose();
    }
    else {
      alert(`필요 아이템 개수 : ${maxMember} / 현재 아이템 개수 : ${sum}\n아이템 개수가 올바르지 않습니다`);
    }
  }

  const [keyword, setKeyword] = React.useState();
  const onChangeValue = (e) => {
    setKeyword(e.target.value);
  }
  const searchGameRoom = () => {
    console.log(keyword);
    //키워드로 검색 dispatch
    dispatch(searchRoom(keyword));
  }

  //style
  const btnMakeRoom = {
    width: '120px',
    height: '40px',
    fontSize: 17,
    fontWeight: 'bold',
    color: '#4F585B',
    bgcolor: '#E9E9E9',
    border: '0.5px solid #4F585B',
    '&:hover': {
      color: '#424242',
      bgcolor: '#DEDEDE',
      border: '0px solid #DCD7C9',
    }
  }

  return (
    <Grid container sx={{ pt: 6 }} spacing={3}>
      <Grid item xs={3}></Grid>
      <Grid item sx={{ p: '2px 4px', display: 'flex', justifyContent: 'center' }} xs={6} >
        <Paper
          component="form"
          sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 530 }}
        >
          <InputBase
            sx={{ ml: 1, flex: 1 }}
            placeholder="방 검색"
            inputProps={{ 'aria-label': 'search game' }}
            onChange={onChangeValue}
          />
          <IconButton type="button" sx={{ p: '10px', color: "#4F585B" }} aria-label="search" onClick={searchGameRoom}>
            <SearchIcon />
          </IconButton>
        </Paper>
      </Grid>
      <Grid item xs={3} sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', justifyContent: 'end' }}>
        <Button variant="outlined" onClick={onRoomInfoModalOpen} sx={btnMakeRoom}>방 만들기</Button>
        <RoomInfoModal canEdit={true} roomData={roomInit} open={open} onClose={onRoomInfoModalClose} onConfirm={onRoomMake} />
      </Grid>
    </Grid>
  )
}