import React from "react";
import { useDispatch, useSelector } from 'react-redux'

import { Avatar, Button, Grid, Paper } from "@mui/material";
import { fetchMemberInfo } from "../../_slice/memberInfoSlice";

export default function MemberInfoForm({ updateProfile }) {
  const [isSetting, setIsSetting] = React.useState(false);
  const dispatch = useDispatch();
  React.useEffect(() => {
    if (!isSetting) {
      setIsSetting(true);
      dispatch(fetchMemberInfo());
    }
  }, []);

  const memberInfo = useSelector(state => state.memberInfo.value.MemberInfo);
  console.log("memberInfo", memberInfo);
  return (
    <Grid container sx={{ pt: 5 }} rowSpacing={2} columnSpacing={{ xs: 1, sm: 2, md: 3 }} justifyContent="center" alignItems="center">
      <Grid item xs={3} >
        <Avatar
          alt="Remy Sharp"
          src="/static/images/avatar/1.jpg"
          sx={{ width: 250, height: 250 }}
        >
        </Avatar>
      </Grid>
      <Grid item xs={7}>
        <Paper sx={{ boxShadow: "none" }}>
          닉네임: {memberInfo.nickname}<br />
          랭킹: {memberInfo.ranking}<br />
          점수: {memberInfo.score}<br />
          전적: {memberInfo.win}승 {memberInfo.lose}패<br />
        </Paper>
      </Grid>
      <Grid item xs={2} sx={{ mt: -20 }}>
        <Button onClick={updateProfile}>프로필 수정</Button>
      </Grid>
    </Grid>
  );
}