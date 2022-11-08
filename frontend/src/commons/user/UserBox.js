import React, { useState } from "react";
import { useSelector } from "react-redux";
import { black } from "../../_css/ReactCSSProperties";
import { Grid, Box } from "@mui/material";
import QuestionMarkIcon from "@mui/icons-material/QuestionMark";

export default function UserBox(props) {
  const [userData, setUserData] = React.useState({
    nickname: "닉네임",
    image: "url",
    ranking: 1,
    score: 100,
    win: 1,
    lose: 0,
  });
  const [width, setWidth] = React.useState(100);
  const [height, setHeight] = React.useState(100);
  const [size, setSize] = React.useState(80);
  React.useEffect(() => {
    if (props.width) {
      setWidth(props.width);
    }
    if (props.height) {
      setHeight(props.height);
    }
    if (props.width && props.height) {
      const tempSize = Math.min(props.width, props.height) * 0.8;
      setSize(tempSize);
      // console.log(tempSize);
    }
    // console.log(props.width, props.height);

    if (props.userData) {
      setUserData(props.userData);
    }
  }, [props]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        width: width,
        height: height,
				border: `7px solid ${black}`,
				borderRadius: "10px"
      }}
    >
      <div sx={{ fontSize: size / 7 }}>{userData.nickname}</div>
      <div>
        {!userData.image || userData.image === "" ? (
          <QuestionMarkIcon sx={{ fontSize: size }} />
        ) : (
          <img
            src={userData.image}
            alt={userData.image}
            style={{ width: size, height: size }}
          />
        )}
      </div>
    </Box>
  );
}
