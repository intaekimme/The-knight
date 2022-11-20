import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, Modal, Box, Grid, TextareaAutosize } from "@mui/material";
import ChatBubbleIcon from '@mui/icons-material/ChatBubble';
import MinimizeIcon from '@mui/icons-material/Minimize';
import {black, white, blue, red, yellow, lightBlue} from "../../../_css/ReactCSSProperties"; 
import {chattingModalHeight, chattingPosition, chattingHeader, chattingBody, chattingInput, chattingSendButton,
  chattingBox, chattingAll, chattingA, chattingB, chattingMine, chattingOthers} from "../../../_css/ChattingCSSProperties"; 
import { onPubChat } from "../../../websocket/RoomPublishes";

// size:int, stompClient, gameId:int
export default function Chatting(props){
  const dispatch = useDispatch();

  // 채팅 모달
	const [open, setOpen] = React.useState(false);
	const onChattingOpen = () => setOpen(true);
	const onChattingClose = () => setOpen(false);

  // chatting data
  const chattingLog = useSelector(state=>state.chatting.chatting);
  const [chatScrollTop, setChatScrollTop] = React.useState(0);
  const [chatScrollHeight, setChatScrollHeight] = React.useState(0);

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
  
  // 스크롤이 맨 아래에 있을 경우 자동 스크롤 down
  React.useEffect(()=>{
    const chattingBodyElement = document.getElementById("chattingBody");
    if(chattingBodyElement && (chatScrollHeight - chatScrollTop) < (chattingModalHeight+10)){
      chattingBodyElement.scrollTop = chattingBodyElement.scrollHeight;
      setChatScrollTop(chattingBodyElement.scrollHeight);
      setChatScrollHeight(chattingBodyElement.scrollHeight);
    }
  }, [chattingLog]);

  //chatting scroll
  const onChatScroll = (e)=>{
    e.preventDefault();
    const chattingBodyElement = e.target;
    setChatScrollTop(chattingBodyElement.scrollTop);
    setChatScrollHeight(chattingBodyElement.scrollHeight);
  }

  // 키 판정
  const onKeyDown = (e)=>{
    // tab
    if(e.keyCode === 9){
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
    if(e.keyCode === 13 && e.shiftKey === false) {
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
    if(!inputMessage || inputMessage===""){
      return;
    }
    const payload = {
      content : inputMessage,
      chattingSet : chattingKind,
    }
    onPubChat({stompClient:props.stompClient, gameId:props.gameId, message:payload});
    console.log(inputMessage);
    document.getElementById("inputArea").value = "";
    setInputMessage("");
  }

  return(
    <div sx={{position:"fixed", bottom:0, left:0}}>
      {open
      ? <div sx={{height:100}}/>
      : <Button onClick={onChattingOpen} sx={{...chattingPosition, color:black, width:120}}><ChatBubbleIcon sx={{fontSize:100, color:white}}/></Button>
      }
      <Modal
        open={open}
        onClose={onChattingClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
        BackdropProps= {{ sx: {background: "none"} }}
      >
        <Box sx={{...chattingPosition}}>
          <Grid container item xs={12} justifyContent="center" alignItems="flex-end"
          sx={{minHeight:100, textAlign:"center"}}>
            <Grid container item xs={12} sx={{...chattingHeader}}>
              <Grid item xs={10} sx={{fontWeight:500, fontSize:size/2, pt:1, pb:1, pl:5}}>Chat</Grid>
              <Grid item xs={2}><Button onClick={onChattingClose} sx={{color:red}}><MinimizeIcon sx={{fontSize:size/2}}/></Button></Grid>
            </Grid>
            <Grid id="chattingBody" container item xs={12} alignItems="flex-end" onScroll={onChatScroll} sx={{...chattingBody}}>
              {/* 채팅로그 */}
              {chattingLog.map((chat, index)=>{
                {
                  let currentChatColor = null;
                  let currentChatSx = {};
                  let currentChatBox = {};
                  let currentJustifyContent = null;
                  switch(chat.chattingSet){
                    case "ALL":
                      currentChatColor = black;
                      currentChatSx = {...currentChatSx, ...chattingAll};
                      currentChatBox = {...chattingBox, background:white, color:black};
                      break;
                    case "A":
                      currentChatColor = red;
                      currentChatSx = {...currentChatSx, ...chattingA};
                      currentChatBox = {...chattingBox, background:currentChatColor, color:white};
                      break;
                    case "B":
                      currentChatColor = blue;
                      currentChatSx = {...currentChatSx, ...chattingB};
                      currentChatBox = {...chattingBox, background:currentChatColor, color:white};
                      break;
                    default:
                      break;
                  }
                  if(chat.memberId.toString()===window.localStorage.getItem("memberId")){
                    currentChatSx = {...currentChatSx, ...chattingMine};
                    currentJustifyContent="flex-end";
                  }
                  else{
                    currentChatSx = {...currentChatSx, ...chattingOthers};
                    currentJustifyContent="flex-start";
                  }
                  
                  const innerHTML = `<div>${chat.content.toString().replaceAll('\n', '</div><div>')}</div>`;
                  const chatText = [...innerHTML.split('</div>')];
                  return (
                    <Grid key={`${chat}${index}`} container item xs={12} justifyContent={currentJustifyContent}
                      sx={{...currentChatSx, color:currentChatColor}}>
                      <Grid item xs={12}>{chat.nickname}</Grid>
                      <Box sx={{...currentChatBox}}>
                        {chatText.map((text, index)=>(
                          <div key={`${chat}${text}${index}`}>{text.replace('<div>', '')}</div>
                        ))}
                      </Box>
                    </Grid>
                  );
                }
              })}
            </Grid>
            <Grid container item xs={12}>
              <Grid item xs={12} sx={{textAlign:"left", background:"#ADADAD"}}>
                <Box sx={{paddingLeft:1, fontSize:size/1.5, border:"1px solid black", backgroundColor:chattingColor, color:white}}>{`[TAB] To `}{chattingKind}</Box>
              </Grid>
              <Grid container item xs={12} sx={{...chattingInput, textAlign:"left", paddingTop:1}}>
                <Grid item xs={9} sx={{position:"relative", top:"7%"}}>
                  <TextareaAutosize aria-label="minimum height" minRows={2} maxRows={3} cols={40}
                    id="inputArea" placeholder="채팅을 입력하세요"
                    onKeyDown={onKeyDown} onChange={onMessageChange}
                    sx={{fontSize:size/2, width:"100%", height:"100%"}}/>
                </Grid>
                <Grid item xs={3} sx={{pb:1, pr:1, textAlign:"right"}}>
                  <Button onClick={onMessageSend} sx={{...chattingSendButton, fontSize:size/3}}>보내기</Button>
                  </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Box>
      </Modal>
    </div>
  );
}