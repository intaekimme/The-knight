import React from "react";
import infoLogo from '../../_assets/infoLogo.png'
import { Box } from "@mui/system";
import { Avatar, Container, Grid, Typography } from "@mui/material";
import video from '../../_assets/info/video.mp4'


export default function Info() {
    return (
        <Container fixed sx={{ width: 1000 }}>
            <Box sx={{
                pt: 10,
                pb: 10,
            }}>
                <Box sx={{ bgcolor: '#fff', opacity: 0.7, height: 800, display: 'flex', justifyContent: 'center', boxShadow: '2px 2px 2px 10px #fff' }}>
                    <Grid container direction="column" alignItems={'center'} spacing={6}>
                    <Grid item>
                    <Avatar src={infoLogo} sx={{ pt: 5, width: 500, height: 'auto' }} variant="square"></Avatar>
                    </Grid>
                    <Grid item>
                    <video controls width="850" style={{opacity: 1}}>
                        <source src={video} type="video/mp4"></source>
                    </video>
                    </Grid>
                    </Grid>
                </Box>
            </Box>
        </Container>
    );
}