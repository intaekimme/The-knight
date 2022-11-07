import * as React from 'react';
import SearchForm from '../components/rank/SearchForm';
import RankTable from "../components/rank/RankTable";

import { Container } from "@mui/system";
import "../_css/Rank.module.css"
import Table from "../components/test/table";

export default function Rank() {
  return (
    <Container fixed >
      {/* <SearchForm/>
      <br /> */}
      <RankTable />
      {/* <Table/> */}
      <br/>
    </Container>
  );
}