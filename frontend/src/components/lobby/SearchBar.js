import React from "react";

import { Button, Grid } from "@mui/material";
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';

export default function SearchBar() {
  return (
    <Grid container sx={{ pt: 5}} spacing={3}>
      <Grid xs={3} spacing={3}></Grid>
      <Grid sx={{ p: '2px 4px', display: 'flex', justifyContent: 'center' }} xs={6} spacing={3}>
        <Paper
        component="form"
        sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 530 }}
        >
        <InputBase
          sx={{ ml: 1, flex: 1 }}
          placeholder="Search Game"
          inputProps={{ 'aria-label': 'search game' }}
          />
        <IconButton type="button" sx={{ p: '10px' }} aria-label="search">
          <SearchIcon />
        </IconButton>
        </Paper>
      </Grid>
      <Grid xs={3} spacing={3} sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', justifyContent:'end' }}>
        <Button variant="outlined">방 만들기</Button>
      </Grid>
    </Grid>
  )
}