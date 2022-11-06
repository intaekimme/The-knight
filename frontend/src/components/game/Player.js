import { useSelector } from "react-redux";
import PersonIcon from "@mui/icons-material/Person";

// 해당 Player가 본인이면 녹색아이콘
function Player({ player }) {
  const me = useSelector(state => state.game.me )

  if (player.memberId === me.memberId) {
    return (
      <div>
        <PersonIcon
          color="success"
          sx={{ fontSize: 100 }}
        />
        <div style={{ color: "green" }}>{player.nickname}</div>
      </div>
    );
  } else {
    return (
      <div>
        <PersonIcon sx={{ fontSize: 100 }} />
        <div>{player.nickname}</div>
      </div>
    );
  }
}

export default Player;
