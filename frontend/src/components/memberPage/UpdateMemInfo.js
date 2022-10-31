import React from "react";
import {useSelector} from 'react-redux'

import { Button, Grid } from "@mui/material";
import { Avatar } from "@mui/material";
import { Box, Stack } from "@mui/system";
import TextField from '@mui/material/TextField';

function changeImg(url) {
}

export default function UpdateMemInfo() {
  const memberInfo = useSelector(state => state.memberInfo.value.MemberInfo);
  const nickname = memberInfo.nickname;
  const image = memberInfo.image;

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
  console.log(nickname);
  return (
    <Box sx={{ flexGrow: 1 }}>
    <Grid container sx={{ pt: 10 }} spacing={3}>
      <Grid item xs={3} spacing={3} >
        <Avatar alt="profile image" src={image} sx={{ width: 250, height: 250 }} />
      </Grid>
        <Grid container item xs={9} spacing={1} >
          {arr.map((a, key) =>{
            return(
              <Grid item xs={2} key={key}>
                <Avatar alt="profile image" src={a} sx={{ width: 110, height: 110 }} onCilck={changeImg(a)} />
              </Grid>
              )
            }
          )}
        </Grid>
      </Grid>
      <Grid sx={{pt:10, display: 'flex', alignItems: 'center', justifyContent:'center'}}>
        닉네임 |&nbsp;
        <TextField
          id="outlined-size-small"
          defaultValue={nickname}
          size="small"
          />
      </Grid>
      <Grid sx={{ pt: 10, display: 'flex', alignItems: 'center', justifyContent:'center'}}>
        <Stack spacing={5} direction="row">
          <Button variant="outlined">프로필 변경</Button>
          <Button variant="outlined">회원탈퇴</Button>
        </Stack>
      </Grid>
    </Box>
  )
}