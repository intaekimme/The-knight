import React from "react";
import { useSelector, useDispatch } from "react-redux";
import MakeRoomModal from "./MakeRoomModal"

import { searchRoom } from "../../_slice/tempGameSlice";

import { Button, Grid } from "@mui/material";
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';

export default function SearchBar() {
  const dispatch = useDispatch();
  // 방설정 모달
  const [open, setOpen] = React.useState(false);
  const roomSettingOpen = () => setOpen(true);
  const roomSettingClose = () => setOpen(false);

  const [keyword, setKeyword] = React.useState();
  const onChangeValue = (e) => {
    setKeyword(e.target.value);
  }
  const searchGameRoom = () => {
    console.log(keyword);
    //키워드로 검색 dispatch
    dispatch(searchRoom(keyword));
  }

  return (
    <Grid container sx={{ pt: 2 }} spacing={3}>
      <Grid item xs={3}></Grid>
      <Grid item sx={{ p: '2px 4px', display: 'flex', justifyContent: 'center' }} xs={6} >
        <Paper
          component="form"
          sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 530 }}
        >
          <InputBase
            sx={{ ml: 1, flex: 1 }}
            placeholder="Search Game"
            inputProps={{ 'aria-label': 'search game' }}
            onChange={onChangeValue}
          />
          <IconButton type="button" sx={{ p: '10px' }} aria-label="search" onClick={searchGameRoom}>
            <SearchIcon />
          </IconButton>
        </Paper>
      </Grid>
      <Grid item xs={3} sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', justifyContent: 'end' }}>
        <Button variant="outlined" onClick={roomSettingOpen}>방 만들기</Button>
        <MakeRoomModal open={open} onClose={roomSettingClose}></MakeRoomModal>
      </Grid>
    </Grid>
  )
}