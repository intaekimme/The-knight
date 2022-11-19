import React from "react";

import { Avatar, Grid } from "@mui/material";

export default function AllianceMem(props) {
  const alliMems = props.alliMems

  return (
    <>
      <Grid container direction="row" alignItems="center" >
        {alliMems.map((alliMem, key) => {
          return (
            <Grid key={key} sx={{ pt: 2, pb: 2, pl: 1, pr: 1 }}>
              <Grid sx={{ pl: '15%' }}>
                <Avatar src={alliMem.image} sx={{ height: 50, width: 50 }}></Avatar>
              </Grid>
              <Grid>
                {alliMem.nickname}
              </Grid>
            </Grid>
          )
        })}
      </Grid>
    </>
  )
}