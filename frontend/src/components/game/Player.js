import PersonIcon from "@mui/icons-material/Person";

// 해당 Player가 본인이면 녹색아이콘
// 추후에 isMe 변수 대신 로그인한 유저와의 비교로 변경
function Player({ player }) {
  if (player.isMine) {
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
