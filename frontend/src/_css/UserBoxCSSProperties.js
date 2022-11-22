import React from "react";
import { white, yellow } from "./ReactCSSProperties";

const userBoxSx = (teamColor, width, height) => {
	return {
		display: "flex",
		position: "relative",
		flexDirection: "column",
		alignItems: "center",
		width: width,
		height: height,
		p: 2,
		border: `7px solid ${teamColor}`,
		borderRadius: "10px",
		background: teamColor
	};
};
const nickname = (size) => { return { fontSize: size / 6, color: white }; };
const userImage = (size) => { return { width: size, height: size }; };
const userBoxReady = (size) => { return { position: "absolute", top: "50%", fontWeight: 900, fontSize: size / 3, color: yellow }; };

export { userBoxSx, nickname, userImage, userBoxReady };