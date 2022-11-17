import * as React from 'react';
import { useSelector, useDispatch } from "react-redux";
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import { searchRank } from "../../_slice/rankSlice";

export default function SearchForm() {
  const dispatch = useDispatch();
  const [keyword, setKeyword] = React.useState();
  const onChangeValue = (e) => {
    setKeyword(e.target.value);
  }
  const searchRankMem = () => {
    console.log(keyword);
    dispatch(searchRank(keyword));
  }
  return (
      <Paper
      component="form"
      sx={{ p: '2px 4px', mt: 2, display: 'flex', alignItems: 'center', width: 500 }}
      >
        <InputBase
          sx={{ ml: 1, flex: 1 }}
          placeholder="닉네임 검색"
          inputProps={{ 'aria-label': 'search name' }}
          onChange={onChangeValue}
        />
        <IconButton type="button" sx={{ p: '10px', color: "#4F585B" }} aria-label="search" onClick={searchRankMem}>
          <SearchIcon />
        </IconButton>
      </Paper>
  );
}