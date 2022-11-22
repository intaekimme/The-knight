import * as React from 'react';
import { useSelector, useDispatch } from "react-redux";

import { Avatar, ListItem, ListItemAvatar, ListItemText, Pagination, Table, TableBody, TableCell, TableContainer, TableFooter, TableHead, TablePagination, TableRow } from "@mui/material";
import { fetchRankAll } from "../../_slice/rankSlice";
import { headerT, dataStyle, dataNameStyle } from "../../_css/RankCSSProperties";


export default function RankTable() {
  const dispatch = useDispatch();
  React.useEffect(() => {
    dispatch(fetchRankAll());
  }, [])
  const rankRows = useSelector(state => state.rank.rankList)
  const [page, setPage] = React.useState(0)
  const [rowsPerPage, setRowsPerPage] = React.useState(20)

  const handleChangePage = (event, newPage) => {
    console.log(newPage);
    setPage(newPage)
  }


  return (
    <>
      {/* <Paper
      component="form"
      sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 500 }}
      >
      <InputBase
        sx={{ ml: 1, flex: 1 }}
        placeholder="Search Name"
        // onChange={(e) => { handleChange(e);  requestSearch(e.target.value) }}
        // onChange={onChangeValue}
        />
      <IconButton type="button" sx={{ p: '10px' }} aria-label="search">
        <SearchIcon />
      </IconButton>
      </Paper>
      <br/> */}
      {/* <Paper style={{ width: '100%' }}>
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={20}
          rowsPerPageOptions={[1]}
          autoHeight={true}
          />
      </Paper> */}
      <TableContainer sx={{ mt: 2, borderRadius: 1, boxShadow: '1px 1px 10px 5px #424242' }}>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow sx={{ bgcolor: '#fff', opacity: 0.6 }}>
              <TableCell align="left" sx={{ ...headerT, pl: 3.5 }} style={{ width: 210 }}>랭킹</TableCell>
              <TableCell align="left" sx={{ ...headerT, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>닉네임</TableCell>
              <TableCell align="center" style={{ ...headerT, width: 210 }}>점수</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {(rowsPerPage > 0
              ? rankRows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              : rankRows
            ).map((row, i) => (
              <TableRow key={i} sx={{ bgcolor: '#424242', opacity: 0.7 }}>
                <TableCell align="left" sx={{ ...dataStyle, pl: 5 }}>{row.ranking}</TableCell>
                <TableCell align="center" sx={{ ...dataNameStyle, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <ListItem sx={{ pl: 30 }}>
                    <ListItemAvatar>
                      <Avatar src={row.image} sx={{ width: 40, height: 40 }} />
                    </ListItemAvatar>
                    <ListItemText>
                      {row.nickname}
                    </ListItemText>
                  </ListItem>
                </TableCell>
                <TableCell sx={dataStyle} align="center">{row.score}</TableCell>
              </TableRow>
            ))}
          </TableBody>
          <TableFooter>
            <TableRow sx={{ bgcolor: '#fff', opacity: 0.6 }}>
              <TablePagination
                count={rankRows.length}
                page={page}
                rowsPerPage={rowsPerPage}
                onPageChange={handleChangePage}
                rowsPerPageOptions={[]}
              />
            </TableRow>
          </TableFooter>
        </Table>
      </TableContainer>
    </>
  );
}