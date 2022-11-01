import React, { useState } from "react";
import { useSelector } from 'react-redux';
import { Button, Box, Grid } from "@mui/material";

export default function ItemBox(props) {
	const size = 150;
	return (
		<Button>
			<Box sx={{width:size, height:size}}>
				{props.text
					? <Grid container>
							<Grid item xs={12} sx={{fontSize:size/10}}>{props.text}</Grid>
							<Grid item xs={12}><img src={props.item} alt={props.item} style={{width:size*0.7, height:size*0.7}} /></Grid>
						</Grid>
					: <img src={props.item} alt={props.item} style={{width:size*0.9, height:size*0.9}} />
				}
			</Box>
		</Button>
	);
}