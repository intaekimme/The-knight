
import { CircularProgress, Box } from '@mui/material';

export default function LoadingPhase() {
  return (
    <div>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
        }}
      >
        <CircularProgress />
        <Box sx={{ padding: "5vmin", fontSize: "3vmin" }}>
          게임 연결 중입니다
        </Box>
      </Box>
    </div>
  )
}