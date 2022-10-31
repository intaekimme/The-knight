import { Grid } from "@mui/material";
import store from "../_store/store";
import { useSelector } from 'react-redux';
import Chatting from "../commons/chatting/Chatting";

export default function Room() {
	const windowData = useSelector((state) => state.windowData.value);
  return (
		<Grid container>
			<Grid item xs={windowData.mainGridWidth}>메인</Grid>
			<Grid item xs={windowData.chatGridWidth}><Chatting /></Grid>
    </Grid>
  );
}