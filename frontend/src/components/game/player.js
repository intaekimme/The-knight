import PersonIcon from '@mui/icons-material/Person';

function Player({ isMe }) {
  if (isMe) {
    return (
      <PersonIcon color="success" sx={{ fontSize: 100 }} />
    )
  }
  else {
    return (
      <PersonIcon sx={{ fontSize: 100 }} />
    )
  }
}

export default Player