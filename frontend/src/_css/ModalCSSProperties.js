import React from "react";
import modalBackground from "../_assets/room/enterRoom.png"

const modalStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  p: 4,
  width: 1000,
  border: '2px solid #000',
  boxShadow: 24,
  bgcolor: 'background.paper',
  backgroundImage: `url(${modalBackground})`,
  backgroundSize: 'cover',
};
const inModalStyle = {
  position: 'relative',
  pt: 1,
  pb: 1,
  pr: 4,
  bgcolor: '#424242',
};
const titleStyle = {
  pr: 2,
  color: "#DCD7C9",
  fontWeight: 'bold',
  textAlign: "right",
  fontSize: 30
};
const infoStyle = {
  pl: 1,
  color: "#DCD7C9",
  fontSize: 25,
};
const itemStyle = {
  color: "#DCD7C9",
  pr:2,
  textAlign:"right",
  fontSize:30
};
const roomInfoTitleStyle = {
  color: "#DCD7C9",
  fontSize: 30,
  fontWeight: 'bold',
}
const buttonStyle = {
  width: '100px',
  height: '60px',
  fontSize: 30,
  fontWeight: 'bold',
  color: '#424242',
  bgcolor: '#DCD7C9',
  border: '0px solid #424242',
  '&:hover':{
    color: '#fff',
    bgcolor: '#4F585B',
    border: '0px solid #DCD7C9',
  }
}

export { modalStyle, inModalStyle, titleStyle, infoStyle, itemStyle, roomInfoTitleStyle, buttonStyle }