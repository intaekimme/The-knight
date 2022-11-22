import React from "react";
import { useSelector, useDispatch } from 'react-redux'
import AllianceMem from "./AllianceMem";
import OppositeMem from "./OppositeMem";

import { Card, Grid, Typography } from "@mui/material";
import { fetchMemberHistory } from "../../_slice/memberInfoSlice";
import { textMaxMem, textResult } from "../../_css/MypageCSSProperties";

export default function CurrentRecord() {
  const dispatch = useDispatch();
  React.useEffect(() => {
    dispatch(fetchMemberHistory());
  }, []);
  const recordInfo = useSelector(state => state.memberInfo.memberHistory)
  console.log("memberReco", recordInfo.games);

  return (
    <Grid container direction="column" sx={{ mb: 10 }}>
      {recordInfo.games && recordInfo.games.map((recordInfoDesc, key) => {
        let capac = '';
        if (recordInfoDesc.capacity === 10) {
          capac = '5:5';
        } else if (recordInfoDesc.capacity === 8) {
          capac = '4:4';
        } else if (recordInfoDesc.capacity === 6) {
          capac = '3:3';
        } else if (recordInfoDesc.capacity === 4) {
          capac = '2:2';
        }
        return (
          <>
            <Grid item sx={{ pt: 6 }} key={key} capac={capac}>
              {
                recordInfoDesc.result === 'WIN' ?
                  <Card sx={{ backgroundColor: '#7487CB', opacity: 0.8, boxShadow: '2px 2px 10px #7487CB' }}>
                    <Grid container alignItems="center">
                      <Grid item sx={{ pl: 3, pr: 5 }}>
                        <Typography sx={textResult}>
                          승
                        </Typography>
                        <Typography sx={textMaxMem}>
                          ({capac})
                        </Typography>
                      </Grid>
                      <Grid item>
                        <AllianceMem alliMems={recordInfoDesc.alliance} />
                      </Grid>
                      <Grid item sx={{ pl: 2, pr: 2 }}>
                        <Typography sx={textMaxMem}>
                          VS
                        </Typography>
                      </Grid>
                      <Grid item>
                        <OppositeMem oppoMems={recordInfoDesc.opposite} />
                      </Grid>
                    </Grid>
                  </Card>
                  :
                  <Card sx={{ backgroundColor: '#CB7474', opacity: 0.8, boxShadow: '2px 2px 10px #CB7474' }}>
                    <Grid container alignItems="center">
                      <Grid item sx={{ pl: 3, pr: 5 }}>
                        <Typography sx={textResult}>
                          패
                        </Typography>
                        <Typography sx={textMaxMem}>
                          ({capac})
                        </Typography>
                      </Grid>
                      <Grid item>
                        <AllianceMem alliMems={recordInfoDesc.alliance} />
                      </Grid>
                      <Grid item sx={{ pl: 2, pr: 2 }}>
                        <Typography sx={textMaxMem}>
                          VS
                        </Typography>
                      </Grid>
                      <Grid item>
                        <OppositeMem oppoMems={recordInfoDesc.opposite} />
                      </Grid>
                    </Grid>
                  </Card>
              }
            </Grid>
          </>
        )
      })}
    </Grid>
  )
}