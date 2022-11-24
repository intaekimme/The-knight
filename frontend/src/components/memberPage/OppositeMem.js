import React from "react";

import { Avatar, Grid } from "@mui/material";

export default function OppositeMem(props) {
  const oppoMems = props.oppoMems;

  function isKorean(nickname) {
    const korean = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
    return korean.test(nickname);
  }

  function shortenNickname(nickname, nicknameLengthMax, koreanNicknameLengthMax) {
    if (isKorean(nickname) && koreanNicknameLengthMax < nickname.length) {
      return nickname.slice(0, koreanNicknameLengthMax - 1) + "...";
    } else if (!isKorean(nickname) && nicknameLengthMax < nickname.length) {
      return nickname.slice(0, nicknameLengthMax - 1) + "...";
    } else {
      return nickname;
    }
  }

  return (
    <Grid
      container
      direction="row"
      alignItems="center"
    >
      {oppoMems.map((oppoMem, key) => {
        return (
          <Grid
            item
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              width: "7.5vmin",
            }}
            key={key}
          >
            <Grid>
              <Avatar
                src={oppoMem.image}
                sx={{ height: "4.5vmin", width: "4.5vmin" }}
              ></Avatar>
            </Grid>
            <Grid sx={{ fontSize: "1vmin" }}>
              {shortenNickname(oppoMem.nickname, props.nicknameLengthMax, props.koreanNicknameLengthMax)}
            </Grid>
          </Grid>
        );
      })}
    </Grid>
  );
}
