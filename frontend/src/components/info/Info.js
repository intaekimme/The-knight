import React from "react";
import infoLogo from '../../_assets/infoLogo.png'
import { Avatar, Container, Grid, Typography, Box, Paper } from "@mui/material";
import video from '../../_assets/info/video.mp4'
import { shadowWhite } from '../../_css/ReactCSSProperties';


export default function Info() {
    return (
        <Container fixed sx={{ width: 1000 }}>
            <Box sx={{
                pt: 10,
                pb: 10,
            }}>
                <Paper sx={{ bgcolor: shadowWhite, height: 800, display: 'flex', justifyContent: 'center', boxShadow: `0 0 5px 5px ${shadowWhite}` }}>
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
                </Paper>
            </Box>
        </Container>
    );
}