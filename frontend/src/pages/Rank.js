import * as React from 'react';
import { Container } from "@mui/system";
import Paper from '@mui/material/Paper';
import { DataGrid } from '@mui/x-data-grid';
import "../_css/Rank.module.css"

import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';

const columns = [
  { field: 'id', headerName: '랭킹', width: 300, headerAlign: 'center', align:'center' },
  { field: 'name', headerName: '닉네임', width: 500, headerAlign: 'center', align:'center' },
  { field: 'score', headerName: '점수', width: 300, headerAlign: 'center', align:'center' },
];

const sample = [
  ['Frozen yoghurt', 159],
  ['Ice cream sandwich', 237],
  ['Eclair', 262],
  ['Cupcake', 305],
  ['Gingerbread', 356],
];

function createData(id, name, score) {
  id = id + 1;
  return { id, name, score };
}

const rows = [];

for (let i = 0; i < 200; i += 1) {
  const randomSelection = sample[Math.floor(Math.random() * sample.length)];
  rows.push(createData(i, ...randomSelection));
}

export default function Rank() {

  return (
    <Container fixed >
      <Paper
      component="form"
      sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 500 }}
    >
      <InputBase
        sx={{ ml: 1, flex: 1 }}
        placeholder="Search Name"
        inputProps={{ 'aria-label': 'search name' }}
      />
      <IconButton type="button" sx={{ p: '10px' }} aria-label="search">
        <SearchIcon />
      </IconButton>
      </Paper>
      <br/>
      <Paper style={{ height: 570, width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={20}
          rowsPerPageOptions={[1]}
      />
          </Paper>
    </Container>
  );
}