
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
        <div>
          연결 중입니다
        </div>
      </Box>
    </div>
  )
}