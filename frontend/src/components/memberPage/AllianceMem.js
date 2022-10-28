import React from "react";

import { Grid } from "@mui/material";

export default function AllianceMem(props) {
  const alliMems = props.alliMems

  return (
    <Grid container>
      {alliMems.map((alliMem, key) => {
        return (
          <Grid key={key}>
            {alliMem.nickname}
          </Grid>
        )
      })}
    </Grid>
  )
}