import { useSelector } from "react-redux";
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";

// 해당 Player가 본인이면 녹색아이콘
function Player({ player }) {
  const me = useSelector((state) => state.game.me);

  if (player.memberId === me.memberId) {
    return (
      <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
        <PersonIcon
          color="success"
          sx={{ fontSize: 100 }}
        />
        <div style={{ color: "green" }}>{player.nickname}</div>
      </Box>
    );
  } else {
    return (
      <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
        <PersonIcon sx={{ fontSize: 100 }} />
        <div>{player.nickname}</div>
      </Box>
    );
  }
}

export default Player;
