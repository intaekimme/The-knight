import PersonIcon from '@mui/icons-material/Person';

function Player({ isMe, nickName }) {
  if (isMe) {
    return (
      <div>
        <PersonIcon color="success" sx={{ fontSize: 100 }} />
        <div style={{ color: "green" }}>{ nickName }</div>
      </div>
    )
  }
  else {
    return (
      <div>
        <PersonIcon sx={{ fontSize: 100 }} />
        <div>{ nickName }</div>
      </div>
    )
  }
}

export default Player