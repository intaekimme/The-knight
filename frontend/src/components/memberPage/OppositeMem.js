import React from "react";

import { Avatar, Grid } from "@mui/material";

export default function OppositeMem(props) {
  const oppoMems = props.oppoMems;

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
            <Grid>{oppoMem.nickname}</Grid>
          </Grid>
        );
      })}
    </Grid>
  );
}
