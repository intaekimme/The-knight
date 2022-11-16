import React from "react";
import { black, white, yellow, lightBlue } from "./ReactCSSProperties";

const chattingPosition ={
  position:"absolute",
  bottom:0, left:0,
}
const chattingHeader = {
  background:yellow,
};
const chattingBody = {
  height:300,
  background:lightBlue,
};
const chattingInput = {
  background:white,
};
const chattingSendButton = {
  fontWeight: 'bold',
  color: '#424242',
  bgcolor: '#DCD7C9',
  border: '0px solid #424242',
  '&:hover':{
    color: '#fff',
    bgcolor: '#4F585B',
    border: '0px solid #DCD7C9',
  },
  width:"20%", height:"100%",
  padding:1
};

export {chattingPosition, chattingHeader, chattingBody, chattingInput, chattingSendButton};