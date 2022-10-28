import React from "react";

import { Grid } from "@mui/material";

export default function OppositeMem(props) {
  const oppoMems = props.oppoMems

  return (
    <Grid container>
      {oppoMems.map((oppoMem, key) => {
        return (
          <Grid key={key}>
            {oppoMem.nickname}
          </Grid>
        )
      })}
    </Grid>
  )
}