import React from "react";
import { useSelector } from 'react-redux'
import AllianceMem from "./AllianceMem";
import OppositeMem from "./OppositeMem";

import { Grid } from "@mui/material";

export default function CurrentRecord() {
  const memberInfo = useSelector(state=>state.memberInfo.value.MemberInfo)
  const recordInfo = memberInfo.currentReco
  console.log("memberReco", recordInfo);

  return (
    <Grid container direction="column">
      {recordInfo.map((recordInfoDesc, key) => {
        let capac = '';
        if (recordInfoDesc.capacity === 10) {
          capac = '5:5';
        } else if(recordInfoDesc.capacity === 8) {
          capac = '4:4';
        } else if(recordInfoDesc.capacity === 6) {
          capac = '3:3';
        } else {
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