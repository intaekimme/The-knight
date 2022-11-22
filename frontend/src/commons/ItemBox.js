import React, { useEffect, useState } from "react";
import { Button, Box, Grid } from "@mui/material";
import { backgroundWhite, white } from "../_css/ReactCSSProperties";

export default function ItemBox(props) {
	// click 함수
	function onClick(params){
		if(props.onClick){
			props.onClick(params);
		}
	}

	React.useEffect(() => { console.log(props.image) }, [props.image]);

	// 버튼 비활성화
	const [buttonDisabled, setButtonDisabled] = React.useState(true);
	useEffect(()=>{
		if(props.buttonDisabled){
			setButtonDisabled(props.buttonDisabled);
		}
	}, [props.buttonDisabled]);
	// box 크기
	const [size, setSize] = useState(100);
	useEffect(()=>{
		if(props.size){
			setSize(props.size);
		}
	}, [props.size]);
	// text 색상
	const [textColor, setTextColor] = useState("blue");
	useEffect(()=>{
		if(props.textColor){
			setTextColor(props.textColor);
		}
	}, [props.textColor]);
	return (
		<Button disabled={buttonDisabled} onClick={(e)=>{onClick(props.params);}} sx={{color:textColor}}>
			<Box sx={{width:size, height:size}}>
				{props.text
					? <Grid container>
							<Grid item xs={12} sx={{fontSize:size/10}}>{props.text}</Grid>
							<Grid item xs={12}><img src={props.image} alt={props.image} style={{width:size*0.7, height:size*0.7, objectFit: "contain", background:white}} /></Grid>
						</Grid>
					: <img src={props.image} alt={props.image} style={{width:size*0.9, height:size*0.9, objectFit: "contain", background:backgroundWhite}} />
				}
			</Box>
		</Button>
	);
}