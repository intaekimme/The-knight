import React from "react";
import infoLogo from '../../_assets/infoLogo.png'
import { Box } from "@mui/system";
import { Avatar, Container, Typography } from "@mui/material";


export default function Info() {
    return (
        <Container fixed sx={{ width: 1000 }}>
            <Box sx={{
                pt: 10,
            }}>
                <Box sx={{ bgcolor: '#fff', opacity: 0.7, height: 1000, display: 'flex', justifyContent: 'center', boxShadow: '2px 2px 2px 10px #fff' }}>
                    <Avatar src={infoLogo} sx={{ pt: 5, width: 'auto', height: 150 }} variant="square"></Avatar>
                </Box>
            </Box>
        </Container>
    );
}