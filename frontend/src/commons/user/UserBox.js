import React from "react";
import { black, red, blue } from "../../_css/ReactCSSProperties";
import { Grid, Box } from "@mui/material";
import QuestionMarkIcon from "@mui/icons-material/QuestionMark";

export default function UserBox(props) {
  // member 초기 데이터
  const [member, setMember] = React.useState({
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
  React.useEffect(()=>{
    if(props.width){
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
    if (props.userData) {
      const resultMember = {...member};
      const tempMember = {...props.userData};
      if(tempMember.id){resultMember.id = tempMember.id;}
      if(tempMember.nickname){resultMember.nickname = tempMember.nickname;}
      if(tempMember.image){resultMember.image = tempMember.image;}
      if(tempMember.readyStatus){resultMember.readyStatus = tempMember.readyStatus;}
      if(tempMember.team){resultMember.team = tempMember.team;
        const tempColor = (tempMember.team==='A') ? red : blue; setTeamColor(tempColor);}
      if(!tempMember.team){setTeamColor(black);}
      if(tempMember.ranking){resultMember.ranking = tempMember.ranking;}
      if(tempMember.score){resultMember.score = tempMember.score;}
      if(tempMember.win){resultMember.win = tempMember.win;}
      if(tempMember.lose){resultMember.lose = tempMember.lose;}
      if(tempMember.empty){resultMember.empty = tempMember.empty;}
      console.log(resultMember);
      setMember(resultMember);
    }
  }, [props.userData]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        width: width,
        height: height,
				border: `7px solid ${teamColor}`,
				borderRadius: "10px"
      }}
    >
      <div sx={{ fontSize: size / 7 }}>{member.nickname}</div>
      <div>
        {member.empty && member.empty === true ? (
          <QuestionMarkIcon sx={{ pt:1, fontSize: size }} />
        ) : (
          <img
            src={member.image}
            // alt={member.image}
            style={{ width: size, height: size }}
          />
        )}
      </div>
    </Box>
  );
}
