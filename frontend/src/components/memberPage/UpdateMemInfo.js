import React from "react";
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux'
import { useDispatch } from 'react-redux'
import { changeImage, deleteMemberInfo, fetchMemberInfo, patchMemberInfo } from '../../_slice/memberInfoSlice'

import { btnLeft, btnRight, memberImg } from '../../_css/MypageCSSProperties'
import styled from 'styled-components';
import { Button, Grid, Typography } from "@mui/material";
import { Avatar } from "@mui/material";
import { Box, Stack } from "@mui/system";
import TextField from '@mui/material/TextField';
import p1 from '../../_assets/mypage/profile/p1.png'
import p2 from '../../_assets/mypage/profile/p2.png'
import p3 from '../../_assets/mypage/profile/p3.png'
import p4 from '../../_assets/mypage/profile/p4.png'
import p5 from '../../_assets/mypage/profile/p5.png'
import p6 from '../../_assets/mypage/profile/p6.png'
import p7 from '../../_assets/mypage/profile/p7.png'
import p8 from '../../_assets/mypage/profile/p8.png'
import p9 from '../../_assets/mypage/profile/p9.png'
import p10 from '../../_assets/mypage/profile/p10.png'
import p11 from '../../_assets/mypage/profile/p11.png'
import p12 from '../../_assets/mypage/profile/p12.png'

// .css-j3098y-MuiAvatar-root
const ImgStyle = styled.div`
  .css-j3098y-MuiAvatar-root:hover {
    background-color: #424242;
    box-shadow: 2px 2px 30px #DCD7C9;
    transition-duration: 0.2s;
    cursor: pointer;
  }
  > a:visited {
    color: black;
    text-decoration: none;
  }
`;

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
    p1,
    p2,
    p3,
    p6,
    p4,
    p5,
    p7,
    p8,
    p9,
    p10,
    p11,
    p12,
  ]
  return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container sx={{ pt: 12 }} spacing={3}>
        <Grid item xs={3} >
          <Avatar alt="profile image" src={image} sx={{ width: 250, height: 250, bgcolor: '#000', border: "1px solid #DCD7C9", boxShadow: '2px 2px 30px #000' }} />
        </Grid>
        <Grid container item xs={9} spacing={1} >
          {arr.map((a, key) => {
            return (
              <Grid item xs={2} key={key}>
                <ImgStyle>
                  <Avatar alt="profile image" src={a} sx={{ width: 110, height: 110, boxShadow: '2px 2px 10px #424242' }} onClick={(e) => { changeImg(a, e) }}></Avatar>
                </ImgStyle>
              </Grid>
            )
          }
          )}
        </Grid>
      </Grid>
      <Grid sx={{ pt: 10, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Typography sx={{ color: '#DCD7C9', fontSize: 24, }}>
          닉네임&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
        </Typography>
        <TextField
          id="outlined-size-small"
          defaultValue={nickname}
          size="small"
          onChange={onChangeNickname}
          sx={{ input: { color: '#424242', fontSize: 24, bgcolor: '#DCD7C9', borderRadius: 1 } }}
        />
      </Grid>
      <Grid sx={{ pt: 15, pb: 12, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Stack spacing={10} direction="row">
          <Button sx={btnLeft} variant="outlined" onClick={updateProfile}>프로필 변경</Button>
          <Button sx={btnRight} variant="outlined" onClick={deleteProfile}>회원탈퇴</Button>
        </Stack>
      </Grid>
    </Box>
  )
}