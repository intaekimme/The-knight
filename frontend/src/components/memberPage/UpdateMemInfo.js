import React from "react";
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux'
import { useDispatch } from 'react-redux'
import { changeImage, deleteMemberInfo, fetchMemberInfo, patchMemberInfo } from '../../_slice/memberInfoSlice'

import { memberImg } from '../../_css/MypageCSSProperties'
import { Button, Grid } from "@mui/material";
import { Avatar } from "@mui/material";
import { Box, Stack } from "@mui/system";
import TextField from '@mui/material/TextField';


export default function UpdateMemInfo() {
  const memberInfo = useSelector(state => state.memberInfo.memberInfo);
  const nickname = memberInfo.nickname;
  let image = memberInfo.image;

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [newUrl, setNewUrl] = React.useState("");
  const [newNickname, setNewNickname] = React.useState("");
  const changeImg = (url, e) => {
    e.preventDefault();
    //프로필 이미지 변경
    setNewUrl(url);
    dispatch(changeImage(url));
  }
  const onChangeNickname = (e) => {
    setNewNickname(e.target.value);
  }
  const updateProfile = () => {
    console.log(newUrl, newNickname);
    if (newUrl === "") {
      dispatch(patchMemberInfo({ newNickname, image }))
    } else if (newNickname === "") {
      dispatch(patchMemberInfo({ nickname, newUrl }))
    } else if (newUrl === "" && newNickname === "") {
      alert("변경사항이 없어용~");
    } else {
      dispatch(patchMemberInfo({ newNickname, newUrl }))
    }
    navigate("/");
  }

  const deleteProfile = () => {
    dispatch(deleteMemberInfo());
    navigate("/");
  }

  const arr = [
    "https://picsum.photos/id/237/200/300",
    "https://picsum.photos/id/236/200/300",
    "https://picsum.photos/id/235/200/300",
    "https://picsum.photos/id/234/200/300",
    "https://picsum.photos/id/233/200/300",
    "https://picsum.photos/id/232/200/300",
    "https://picsum.photos/id/231/200/300",
    "https://picsum.photos/id/230/200/300",
    "https://picsum.photos/id/229/200/300",
    "https://picsum.photos/id/228/200/300",
    "https://picsum.photos/id/227/200/300",
    "https://picsum.photos/id/238/200/300",
  ]
  return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container sx={{ pt: 10 }} spacing={3}>
        <Grid item xs={3} >
          <Avatar alt="profile image" src={image} sx={{ width: 250, height: 250 }} />
        </Grid>
        <Grid container item xs={9} spacing={1} >
          {arr.map((a, key) => {
            return (
              <Grid item xs={2} key={key}>
                <Avatar alt="profile image" src={a} sx={{ width: 110, height: 110 }} onClick={(e) => { changeImg(a, e) }}></Avatar>
              </Grid>
            )
          }
          )}
        </Grid>
      </Grid>
      <Grid sx={{ pt: 10, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        닉네임 |&nbsp;
        <TextField
          id="outlined-size-small"
          defaultValue={nickname}
          size="small"
          onChange={onChangeNickname}
        />
      </Grid>
      <Grid sx={{ pt: 10, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Stack spacing={5} direction="row">
          <Button variant="outlined" onClick={updateProfile}>프로필 변경</Button>
          <Button variant="outlined" onClick={deleteProfile}>회원탈퇴</Button>
        </Stack>
      </Grid>
    </Box>
  )
}