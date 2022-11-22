import React from "react";
import { backgroundWhite } from "./ReactCSSProperties";
import modalBackground from "../_assets/room/roomInfo.png"

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
  color: backgroundWhite,
  fontWeight: 'bold',
  textAlign: "right",
  fontSize: 20
};
const infoStyle = {
  pl: 1,
  color: backgroundWhite,
  fontSize: 18,
  height: 38,
};
const itemStyle = {
  color: backgroundWhite,
  textAlign: "right",
  fontSize: 18
};
const roomInfoTitleStyle = {
  color: backgroundWhite,
  fontSize: 25,
  fontWeight: 'bold',
}
const buttonStyle = {
  width: '90px',
  height: '40px',
  fontSize: 18,
  fontWeight: 'bold',
  color: '#424242',
  bgcolor: backgroundWhite,
  border: '0px solid #424242',
  '&:hover': {
    color: '#fff',
    bgcolor: '#4F585B',
    border: `0px solid ${backgroundWhite}`,
  }
}

export { modalStyle, inModalStyle, titleStyle, infoStyle, itemStyle, roomInfoTitleStyle, buttonStyle }