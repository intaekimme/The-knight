import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, Modal, Box, Grid, TextareaAutosize } from "@mui/material";
import MessageIcon from '@mui/icons-material/Message';
import MinimizeIcon from '@mui/icons-material/Minimize';
import {black, white, blue, red, yellow, lightBlue} from "../../../_css/ReactCSSProperties"; 
import {chattingPosition, chattingHeader, chattingBody, chattingInput, chattingSendButton} from "../../../_css/ChattingCSSProperties"; 
import { onPubChat } from "../../../websocket/RoomPublishes";

// size:int, stompClient, gameId:int
export default function Chatting(props){
  const dispatch = useDispatch();

  const chattingLog = useSelector(state=>state.chatting.chatting);

  // 채팅 모달
	const [open, setOpen] = React.useState(false);
	const onChattingOpen = () => setOpen(true);
	const onChattingClose = () => setOpen(false);

  // 현재 방의 member들
  const members = useSelector(state=>state.room.usersInfo);
  // 팀 A/B
  const [thisTeam, setThisTeam] = React.useState("A");
  // 채팅 종류
  const [chattingKind, setChattingKind] = React.useState("ALL");
  // 채팅 색깔
  const [chattingColor, setChattingColor] = React.useState(black);
  // 입력 메세지
  const [inputMessage, setInputMessage] = React.useState("");

  // 크기
	const [size, setSize] = React.useState(props.size);
	React.useEffect(()=>{
		if(props.size){
			setSize(props.size);
		}
	}, [props.size]);

  // 채팅 종류 바꿀때마다 채팅 색깔 설정
  React.useEffect(()=>{
    if(chattingKind==="ALL"){
      setChattingColor(black);
    }
    if(chattingKind==="A" || chattingKind==="B"){
      setChattingColor(thisTeam==="A" ? red : blue);
    }
  }, [chattingKind, thisTeam]);

  // members 정보 update 마다 팀 색깔 설정
  React.useEffect(()=>{
    for(let i=0;i<members.length;i++){
      if(members[i].id.toString() === window.localStorage.getItem("memberId")){
        const tempTeam = members[i].team;
        setThisTeam(tempTeam);
        break;
      }
    }
  }, [members]);

  // 키 판정
  const onKeyDown = (e)=>{
    // tab
    if(e.keyCode == 9){
      e.preventDefault();
      let tempKind = chattingKind;
      switch(tempKind){
        case "ALL":
          tempKind=thisTeam;
          break;
        case "A": case "B":
          tempKind="ALL";
          break;
        default:
          break;
      }
      setChattingKind(tempKind);
    }
    // enter
    if(e.keyCode == 13 && e.shiftKey == false) {
      e.preventDefault();
      onMessageSend();
    }
  }

  // input message update
  const onMessageChange = (e)=>{
    const currentMessage = e.target.value;
    setInputMessage(currentMessage);
  }

  // 메세지 전송
  const onMessageSend = ()=>{
    const payload = {
      content : inputMessage,
      chattingSet : chattingKind,
    }
    onPubChat({stompClient:props.stompClient, gameId:props.gameId, message:payload});
    console.log(inputMessage);
  }

  return(
    <div sx={{position:"absolute", bottom:0, left:0}}>
      {open
      ? <div sx={{height:100}}/>
      : <Button onClick={onChattingOpen} sx={{...chattingPosition, color:black}}><MessageIcon sx={{fontSize:100}}/></Button>
      }
      <Modal
        open={open}
        onClose={onChattingClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
        BackdropProps= {{ sx: {background: "none"} }}
      >
        <Box sx={{...chattingPosition, width:300}}>
          <Grid container item xs={12} justifyContent="center" alignItems="flex-end"
          sx={{minHeight:100, textAlign:"center"}}>
            <Grid container item xs={12} sx={{...chattingHeader}}>
              <Grid item xs={10} sx={{fontSize:size/2, paddingLeft:5}}>채팅</Grid>
              <Grid item xs={2}><Button onClick={onChattingClose} sx={{color:red}}><MinimizeIcon sx={{fontSize:size/2}}/></Button></Grid>
            </Grid>
            <Grid item xs={12} sx={{...chattingBody}}>
              {/* 채팅로그 */}
              {chattingLog.map((chat, index)=>(
                <div>{chat.content}</div>
              ))}
            </Grid>
            <Grid container item xs={12} sx={{border:"1px solid black"}}>
              <Grid item xs={12} sx={{textAlign:"left", background:"#ADADAD"}}>
                <Box sx={{fontSize:size/3, border:"1px solid black", color:chattingColor}}>{chattingKind}</Box>
              </Grid>
              <Grid container item xs={12} sx={{...chattingInput, textAlign:"left", paddingTop:1}}>
                <Grid item xs={9} sx={{position:"relative", top:"7%"}}>
                  <TextareaAutosize aria-label="minimum height" minRows={2} maxRows={3} placeholder="채팅을 입력하세요"
                    onKeyDown={onKeyDown} onChange={onMessageChange}
                    sx={{fontSize:size/4, width:"100%", height:"100%", padding:1}}/>
                </Grid>
                <Grid item xs={3}>
                  <Button onClick={onMessageSend} sx={{...chattingSendButton, fontSize:size/4}}>보내기</Button>
                  </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Box>
      </Modal>
    </div>
  );
}