import { Avatar, Card, CardActions, Container, Grid, Typography } from "@mui/material";
import { Box } from "@mui/system";
import React from "react";

import fullLogo from '../_assets/fullLogo.png'
import styled from '../_css/Main.module.css'
import { btnMain } from '../_css/MainCSSProperties'
import KeyboardDoubleArrowRightIcon from '@mui/icons-material/KeyboardDoubleArrowRight';

export default function Main() {
  return (
    <div className={styled.imgMain}>
    <Container fixed sx={{width: 1000}}>
        <Box sx={{
          pt: 10,
        // '&:hover': {
        //   backgroundColor: 'primary.main',
        //   opacity: [0.9, 0.8, 0.7],
        // },
      }}>
        <Box sx={{ bgcolor: '#fff', opacity: 0.7, height: 647, display: 'flex', justifyContent: 'center' }}>
          <Grid>
            <Avatar src={fullLogo} sx={{ pb: 3, height: 400, width: 'auto'}}></Avatar>
              <Card sx={btnMain}>
                <Box sx={{ display: "flex", alignItems: "center", justifyContent:'space-between', width: "100%", height: "100%" }}>
                  <Box>
                    게임하러 가기
                  </Box>
                  <Box>
                    <KeyboardDoubleArrowRightIcon fontSize="large"/>
                  </Box>
                </Box>
              </Card>
              <br/>
            <Card>
              게임 방법
            </Card>
          </Grid>
        </Box>
      </Box>
    </Container>
    </div>
  );
}