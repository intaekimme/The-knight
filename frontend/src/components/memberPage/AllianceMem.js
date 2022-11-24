import React from "react";

import { Avatar, Grid } from "@mui/material";

export default function AllianceMem(props) {
  const alliMems = props.alliMems;

  return (
    <>
      <Grid
        container
        direction="row"
        alignItems="center"
      >
        {alliMems.map((alliMem, key) => {
          return (
            <Grid
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
                  src={alliMem.image}
                  sx={{ height: "4.5vmin", width: "4.5vmin" }}
                ></Avatar>
              </Grid>
              <Grid>{alliMem.nickname}</Grid>
            </Grid>
          );
        })}
      </Grid>
    </>
  );
}
