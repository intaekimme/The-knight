import React, { useEffect, useState } from "react";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import { Grid, Box, Input, Button } from "@mui/material";

export default function Chatting(){
  const [chattingKind, setChattingKind] = React.useState("전체");
  const [chattingColor, setChattingColor] = React.useState("black");
  const chattingColorList = ["black", "blue"];
  React.useEffect(()=>{
    if(chattingKind==="전체"){
      setChattingColor("black");
    }
    if(chattingKind==="팀"){
      setChattingColor("blue");
    }
  }, [chattingKind]);
  return(
    <Grid container item xs={12} justifyContent="center" alignItems="flex-end"
    sx={{minHeight:100, textAlign:"center"}}>
      <Grid item xs={12} sx={{minHeight:300}}>채팅</Grid>
      <Grid item xs={12} sx={{textAlign:"left"}}>
        <Box sx={{border:"1px solid black", color:chattingColor}}>{chattingKind}</Box>
      </Grid>
      <Grid item xs={12} sx={{minHeight:100, textAlign:"left"}}>
        <Input placeholder="채팅을 입력하세요" sx={{width:"80%", height:"100%"}}/>
        <Button sx={{border:"1px solid black", width:"20%", height:"100%"}}>보내기</Button>
      </Grid>
    </Grid>
  );
}