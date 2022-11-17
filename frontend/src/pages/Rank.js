import * as React from 'react';
import SearchForm from '../components/rank/SearchForm';
import RankTable from "../components/rank/RankTable";

import { Container } from "@mui/system";
import "../_css/Rank.module.css"
import styled from "../_css/Rank.module.css"

export default function Rank() {
  return (
    <div className={styled.imgRank}>
    <Container fixed >
      <br/>
      <SearchForm />
      <br />
      <RankTable />
      {/* <Table/> */}
      <br/>
    </Container>
    </div>
  );
}