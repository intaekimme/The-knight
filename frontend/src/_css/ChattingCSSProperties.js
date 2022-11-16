import React from "react";
import { black, white, yellow, lightBlue } from "./ReactCSSProperties";

const chattingModalWidth = 300;
const chattingModalHeight = 600;
const chattingPosition ={
  position:"fixed",
  bottom:0, left:0,
  width: chattingModalWidth,
}
const chattingHeader = {
  background:yellow,
};
const chattingBody = {
  maxHeight:chattingModalHeight,
  background:"#DCD7C9",
};
const chattingInput = {
  background:white,
};
const chattingSendButton = {
  fontWeight: 'bold',
  color: '#424242',
  background: '#DCD7C9',
  border: '0px solid #424242',
  '&:hover':{
    color: '#fff',
    background: '#4F585B',
    border: '0px solid #DCD7C9',
  },
  width:"20%", height:"100%",
  padding:1,
};
const chattingBox ={
  background: '#4F585B',
  borderRadius: 10,
  width: chattingModalWidth*0.8,
  padding:1,
  fontWeight:900,
}
const chattingAll = {
  color: black,
}
const chattingA = {
  color: black,
}
const chattingB = {
  color: black,
}
const chattingMine = {
  textAlign: "right",
  marginTop: 1,
  marginRight: 1,
}
const chattingOthers = {
  textAlign: "left",
  marginTop: 1,
  marginLeft: 1,
}

export {chattingPosition, chattingHeader, chattingBody, chattingInput, chattingSendButton,
  chattingBox, chattingAll, chattingA, chattingB, chattingMine, chattingOthers};