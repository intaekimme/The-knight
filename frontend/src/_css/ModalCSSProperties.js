import React from "react";
import modalBackground from "../_assets/room/enterRoom.png"

const modalStyle = {
  // position: 'absolute',
  // top: '50%',
  // left: '50%',
  // transform: 'translate(-50%, -50%)',
  // p: 4,
  // width: 800,
  // border: '0px solid #000',
  // boxShadow: 25,
  // bgcolor: 'background.paper',
  // backgroundImage: `url(${modalBackground})`,
  // backgroundSize: 'cover',
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50vw',
  height: '32vw',
  // border: '1px solid #000',
  borderRadius: 2,
  boxShadow: '2px 2px 10px #000',
  p: '10px 36px 36px 36px',
  backgroundImage: `url(${modalBackground})`,
  backgroundSize: 'cover',
};
const inModalStyle = {
  position: 'relative',
  pt: 1,
  pb: 1,
  pr: 4,
  bgcolor: '#424242',
  // position: 'relative',
  // 	pt: 1,
  // 	pb: 1,
  width: '50vw',
  height: '24vw',
  // 	bgcolor: '#424242',
};
const titleStyle = {
  pr: 2,
  color: "#DCD7C9",
  fontWeight: 'bold',
  textAlign: "right",
  fontSize: 20
};
const infoStyle = {
  pl: 1,
  color: "#DCD7C9",
  fontSize: 18,
  height: 38,
};
const itemStyle = {
  color: "#DCD7C9",
  textAlign: "right",
  fontSize: 18
};
const roomInfoTitleStyle = {
  color: "#DCD7C9",
  fontSize: 25,
  fontWeight: 'bold',
}
const buttonStyle = {
  width: '90px',
  height: '40px',
  fontSize: 18,
  fontWeight: 'bold',
  color: '#424242',
  bgcolor: '#DCD7C9',
  border: '0px solid #424242',
  '&:hover': {
    color: '#fff',
    bgcolor: '#4F585B',
    border: '0px solid #DCD7C9',
  }
}

export { modalStyle, inModalStyle, titleStyle, infoStyle, itemStyle, roomInfoTitleStyle, buttonStyle }