import React from "react";
import { useDispatch, useSelector } from 'react-redux'

import { btnModify, cardMem, formMem, memberImg } from "../../_css/MypageCSSProperties"
import { Avatar, Button, Card, Grid, Paper, Typography } from "@mui/material";
import { fetchMemberInfo } from "../../_slice/memberInfoSlice";

export default function MemberInfoForm({ updateProfile }) {
  // const [isSetting, setIsSetting] = React.useState(false);
  const dispatch = useDispatch();
  React.useEffect(() => {
    //   console.log(isSetting);
    //   if (!isSetting) {
    //     setIsSetting(true);
    dispatch(fetchMemberInfo());
    //   }
  }, []);
  const memberInfo = useSelector(state => state.memberInfo.memberInfo);
  console.log("memberInfo", memberInfo);
  return (
    <Grid container sx={{ pt: 5 }} rowSpacing={2} columnSpacing={{ xs: 1, sm: 2, md: 3 }} justifyContent="center" alignItems="center">
      <Grid item xs={3} >
        <Avatar
          alt="Remy Sharp"
          src={memberInfo.image}
          sx={{ width: 250, height: 250, bgcolor: '#000', memberImg }}
          style={{ border: "1px solid #DCD7C9", boxShadow: '2px 2px 10px #424242' }}
        >
        </Avatar>
      </Grid>
      <Grid item xs={7}>
        <Card sx={{ ...cardMem, pt: 4, pb: 3, pl: 22, ml: '-150px' }}>
          <Grid container direction="column" spacing={2}>
            <Grid item sx={formMem}>
              닉네임: {memberInfo.nickname}
            </Grid>
            <Grid item sx={formMem}>
              랭킹: {memberInfo.ranking} 위<br />
            </Grid>
            <Grid item sx={formMem}>
              점수: {memberInfo.score} 점<br />
            </Grid>
            <Grid item sx={formMem}>
              전적: {memberInfo.win}승 {memberInfo.lose}패<br />
            </Grid>
          </Grid>
          <Grid sx={{ pr: 5, mt: '-25px', display: 'flex', justifyContent: 'end' }}>
            <Button sx={btnModify} onClick={updateProfile}>프로필 편집</Button>
          </Grid>
        </Card >
      </Grid>
    </Grid >
  );
}