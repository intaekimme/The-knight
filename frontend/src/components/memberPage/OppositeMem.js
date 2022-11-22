import React from "react";

import { Avatar, Grid } from "@mui/material";

export default function OppositeMem(props) {
  const oppoMems = props.oppoMems

  return (
    <Grid container direction="row" alignItems="center">
      {oppoMems.map((oppoMem, key) => {
        return (
          <Grid item key={key} sx={{ pt: 2, pb: 2, pl: 1, pr: 1 }}>
            <Grid sx={{ pl: '15%' }}>
              <Avatar src={oppoMem.image} sx={{ height: 50, width: 50 }}></Avatar>
            </Grid>
            <Grid>
              {oppoMem.nickname}
            </Grid>
          </Grid>
        )
      })}
    </Grid>
  )
}