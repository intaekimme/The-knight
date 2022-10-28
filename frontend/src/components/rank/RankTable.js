import * as React from 'react';

import Paper from '@mui/material/Paper';
import { DataGrid } from '@mui/x-data-grid';

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

export default function RankTable() {

  return (
      <Paper style={{ width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={20}
          rowsPerPageOptions={[1]}
          autoHeight={true}
      />
      </Paper>
  );
}