import * as React from 'react';

import Paper from '@mui/material/Paper';
// import { DataGrid } from '@mui/x-data-grid';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import { Table, TableBody, TableCell, TableContainer, TableFooter, TableHead, TablePagination, TableRow } from "@mui/material";

// const columns = [
//   { field: 'id', headerName: '랭킹', width: 300, headerAlign: 'center', align:'center' },
//   { field: 'name', headerName: '닉네임', width: 500, headerAlign: 'center', align:'center' },
//   { field: 'score', headerName: '점수', width: 300, headerAlign: 'center', align:'center' },
// ];

// const sample = [
//   ['Frozen yoghurt', 159],
//   ['Ice cream sandwich', 237],
//   ['Eclair', 262],
//   ['Cupcake', 305],
//   ['Gingerbread', 356],
// ];

// function createData(id, name, score) {
//   id = id + 1;
//   return { id, name, score };
// }

// const rows = [];
// for (let i = 0; i < 200; i += 1) {
//   const randomSelection = sample[Math.floor(Math.random() * sample.length)];
//   rows.push(createData(i, ...randomSelection));
// }
const originalRows = [
  { rank: 1, name: "Pizza", score: 200},
  { rank: 2, name: "Pizza", score: 200},
  { rank: 3, name: "Pizza", score: 200},
  { rank: 4, name: "Pizza", score: 200},
  { rank: 5, name: "Pizza", score: 200},
  { rank: 6, name: "Pizza", score: 200},
  { rank: 7, name: "Pizza", score: 200},
  { rank: 8, name: "Pizza", score: 200},
  { rank: 9, name: "chicken", score: 200},
  { rank: 10, name: "Pizza", score: 200},
  { rank: 11, name: "Pizza", score: 200},
  { rank: 12, name: "Pizza", score: 200},
  { rank: 13, name: "Pizza", score: 200},
  { rank: 14, name: "Pizza", score: 200},
  { rank: 15, name: "Pizza", score: 200},
  { rank: 16, name: "Pizza", score: 200},
  { rank: 17, name: "Pizza", score: 200},
  { rank: 18, name: "Pizza", score: 200},
  { rank: 19, name: "Pizza", score: 200},
  { rank: 20, name: "Pizza", score: 200},
  { rank: 21, name: "Pizza", score: 200},
  { rank: 22, name: "Pizza", score: 200},
  { rank: 23, name: "Pizza", score: 200},
  { rank: 24, name: "Pizza", score: 200},
];

export default function RankTable() {
  const [rows, setRows] = React.useState(originalRows);
  const [searched, setSearched] = React.useState("");
  const [page, setPage] = React.useState(0)
  const [rowsPerPage, setRowsPerPage] = React.useState(20)

  const handleChange = (event) => {
    setSearched(event.target.value);
  };

  const requestSearch = (searchedVal) => {
    console.log(searchedVal);
    const filteredRows = originalRows.filter((row) => {
      return row.name.toLowerCase().includes(searchedVal.toString().toLowerCase());
    });
    setRows(filteredRows);
  };

  const handleChangePage = (event, newPage) => {
    console.log(newPage);
    setPage(newPage)
  }

  return (
    <>
    <Paper
      component="form"
      sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 500 }}
      >
      <InputBase
        sx={{ ml: 1, flex: 1 }}
        placeholder="Search Name"
        value={searched}
        onChange={(e) => { handleChange(e);  requestSearch(e.target.value) }}
        />
      <IconButton type="button" sx={{ p: '10px' }} aria-label="search">
        <SearchIcon />
      </IconButton>
      </Paper>
      <br/>
      {/* <Paper style={{ width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={20}
          rowsPerPageOptions={[1]}
          autoHeight={true}
          />
      </Paper> */}
      <TableContainer>
          <Table aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell align="center" style={{ width: 160 }}>랭킹</TableCell>
                <TableCell align="center">닉네임</TableCell>
                <TableCell align="center" style={{ width: 160 }}>점수</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
            {rows
              .slice(page * rowsPerPage, (page + 1) * rowsPerPage)
              .map((row, i) => (
                <TableRow key={row.rank}>
                  <TableCell align="center">{page * rowsPerPage + i + 1}</TableCell>
                  <TableCell align="center">{row.name}</TableCell>
                  <TableCell align="center">{row.score}</TableCell>
                </TableRow>
              ))}
            </TableBody>
            <TableFooter>
              <TableRow>
                <TablePagination
                  count={rows.length}
                  page={page}
                  rowsPerPage={rowsPerPage}
                  onChangePage={handleChangePage}
                  rowsPerPageOptions={[]}
                />
              </TableRow>
            </TableFooter>
          </Table>
        </TableContainer>
          </>
  );
}