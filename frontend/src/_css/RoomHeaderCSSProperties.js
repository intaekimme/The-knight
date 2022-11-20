import React from "react";
import { white } from "./ReactCSSProperties";

const leftInfoGrid = (size) => { return { fontSize: size, display: "flex", alignItems: "center", color: white }; };
const settingIcon = (size) => { return { color: "#ADADAD", fontSize: size * 2 }; };
const rightInfoGrid = (size) => { return { fontSize: size * 1.2, display: "flex", justifyContent: "center", alignItems: "center", color: '#424242' }; };
const selectTeamBox = (size) => { return { width: size * 10, height: size * 2.5, border: `7px solid #DCD7C9`, borderRadius: "10px", bgcolor: '#DCD7C9', display: "flex", alignItems: "center", justifyContent: "center", boxShadow: '-1px 1px 30px #DCD7C9', fontSize: 25 }; };
const selectTeamButton = (size) => { return { margin: 0.5, color: white, fontSize: size, minWidth: size * 2, width: size * 2, height: size * 2, borderRadius: "50%" }; };
const exitGrid = (size) => { return { display: "flex", justifyContent: "center", alignItems: "center" }; };
const exitIcon = (size) => { return { color: "#FF5555", fontSize: size * 1.5 }; };

export { leftInfoGrid, settingIcon, rightInfoGrid, selectTeamBox, selectTeamButton, exitGrid, exitIcon };