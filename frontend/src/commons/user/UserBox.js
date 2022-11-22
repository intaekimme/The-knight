import React from "react";
import { Grid, Box } from "@mui/material";
import QuestionMarkIcon from "@mui/icons-material/QuestionMark";
import { black, red, blue, yellow, white } from "../../_css/ReactCSSProperties";
import { userBoxSx, nickname, userImage, userBoxReady } from "../../_css/UserBoxCSSProperties";

export default function UserBox(props) {
  // user 초기 데이터
  const [user, setUser] = React.useState({
    id: -1,
    nickname: "",
    image: "",
    readyStatus: false,
    team: 'A',
    ranking: -1,
    score: -1,
    win: -1,
    lose: -1,
    empty: false,
  });
  // 가로세로, 초기사이즈
  const [width, setWidth] = React.useState(100);
  const [height, setHeight] = React.useState(100);
  const [size, setSize] = React.useState(80);
  // 초기 team 컬러
  const [teamColor, setTeamColor] = React.useState(black);
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
    }
  }, [props.width, props.height]);
  React.useEffect(() => {
    console.log(props.userData);
    if (props.userData) {
      const tempUser = { ...user };
      if (props.userData.id !== undefined) { tempUser.id = props.userData.id; }
      if (props.userData.nickname !== undefined) { tempUser.nickname = props.userData.nickname; }
      if (props.userData.image !== undefined) { tempUser.image = props.userData.image; }
      if (props.userData.readyStatus !== undefined) { tempUser.readyStatus = props.userData.readyStatus; }
      if (props.userData.team !== undefined) {
        tempUser.team = props.userData.team;
        const tempColor = (!props.userData.team || props.userData.team === '') ? white : (props.userData.team === 'A') ? red : blue; setTeamColor(tempColor);
      }
      if (props.userData.team === undefined) { setTeamColor(black); }
      if (props.userData.ranking !== undefined) { tempUser.ranking = props.userData.ranking; }
      if (props.userData.score !== undefined) { tempUser.score = props.userData.score; }
      if (props.userData.win !== undefined) { tempUser.win = props.userData.win; }
      if (props.userData.lose !== undefined) { tempUser.lose = props.userData.lose; }
      if (props.userData.empty !== undefined) { tempUser.empty = props.userData.empty; }
      setUser(tempUser);
    }
  }, [props.userData]);

  return (
    <Box sx={{ ...userBoxSx(teamColor, width, height) }}>
      <div style={{ ...nickname(size), paddingBottom: '10px', marginTop: '-8px' }}>{user.nickname}</div>
      <div>
        {user.empty && user.empty === true ? (
          <QuestionMarkIcon sx={{ pt: 1, fontSize: size, color: '#6A6969' }} />
        ) : (
          <img
            src={user.image}
            // alt={user.image}
            style={{ ...userImage(size) }}
          />
        )}
      </div>
      <div style={{ ...userBoxReady(size) }}>{user.readyStatus ? "Ready" : ""}</div>
    </Box>
  );
}
