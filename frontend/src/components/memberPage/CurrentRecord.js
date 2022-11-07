import React from "react";
import { useSelector, useDispatch } from 'react-redux'
import AllianceMem from "./AllianceMem";
import OppositeMem from "./OppositeMem";

import { Grid } from "@mui/material";
import { fetchMemberHistory } from "../../_slice/memberInfoSlice";

export default function CurrentRecord() {
  const dispatch = useDispatch();
  React.useEffect(() => {
        dispatch(fetchMemberHistory());
    }, []);
  const recordInfo = useSelector(state => state.memberInfo.memberHistory)
  console.log("memberReco", recordInfo.games);

  return (
    <Grid container direction="column" sx={{mb:10}}>
      {recordInfo.games && recordInfo.games.map((recordInfoDesc, key) => {
        let capac = '';
        if (recordInfoDesc.capacity === 10) {
          capac = '5:5';
        } else if(recordInfoDesc.capacity === 8) {
          capac = '4:4';
        } else if(recordInfoDesc.capacity === 6) {
          capac = '3:3';
        } else if(recordInfoDesc.capacity === 4) {
          capac = '2:2';
        }
        return (
          <Grid item sx={{ pt:5}} key={key} capac={capac}>
            <h1>
              {recordInfoDesc.result}
              {capac}
            </h1>
            <AllianceMem alliMems={recordInfoDesc.alliance} />
            <OppositeMem oppoMems={recordInfoDesc.opposite} />
          </Grid>
        )
      })}
    </Grid>
  )
}