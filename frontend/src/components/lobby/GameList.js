import React from "react";
import { useDispatch, useSelector } from "react-redux";

import { Button, Card, CardActions, CardContent, CardHeader, Grid, Pagination, Typography } from "@mui/material";
import GameDescModal from "./GameDescModal";
import { gameDesc, gameListAll } from "../../_slice/tempGameSlice";

export default function GameList() {
  const [isSetting, setIsSetting] = React.useState(false);
  const dispatch = useDispatch();
  React.useEffect(() => {
    if (!isSetting) {
      setIsSetting(true);
      dispatch(gameListAll());
    }
  }, []);
  //temp data
  // const gameList = useSelector(state => state.tempGame.value.gameList)

  //real data
  const gameList = useSelector(state => state.tempGame.gameListAll)
  console.log("gmaelistall", gameList);


  // 방설정 모달
  const [open, setOpen] = React.useState(false);
  const roomSettingOpen = () => setOpen(true);
  const roomSettingClose = () => setOpen(false);

  // gameId props
  const [id, setId] = React.useState(0);
  const fetchGameInfo = (gameId, e) => {
    e.preventDefault();
    console.log("gameId", gameId);
    setId(gameId);
    //gameId로 게임 상세정보 불러오기
    dispatch(gameDesc(gameId));
  }
  //저장되어있는 게임 상세정보 store에서 불러오기
  // const bla =
  // {
  //   gameId: 1,
  //   title: '블라'
  // }

  // function gameListRender(gameList) {
  //   let arr = []
  //   console.log("ssss", gameList);
  //   for (let index = 0; index < gameList.length; index++) {
  //     arr.push(
  //       <Grid item key={index}>
  //           <Card sx={{ width: '23vw', height: '14vw' }} onClick={() => { roomSettingOpen(); fetchGameInfo(gameList[index].gameId); }}>
  //             {gameList[index].title}
  //           </Card>
  //       </Grid>
  //     )
  //   }
  //   return arr
  // }

  //pagination
  const [page, setPage] = React.useState(0)
  const [rowsPerPage, setRowsPerPage] = React.useState(6)

  const handleChangePage = (event, newPage) => {
    console.log(newPage);
    setPage(newPage)
    dispatch(gameListAll(page - 1));
  }

  //style
  const btnStyle = {
    width: '90px',
    height: '25px',
    fontSize: 13,
    color: '#fff',
    bgcolor: '#4F585B',
    border: '0px solid #424242',
    '&:hover': {
      color: '#4F585B',
      bgcolor: '#fff',
      border: '0.5px solid #4F585B',
    }
  }
  const nobtnStyle = {
    width: '90px',
    height: '25px',
    fontSize: 13,
    color: '#fff',
    bgcolor: '#302E24',
    border: '0px solid #424242',
    '&:hover': {
      color: '#4F585B',
      bgcolor: '#fff',
      border: '0.5px solid #4F585B',
    }
  }

  return (
    <div>
      <Grid container spacing={3} columnSpacing={5} sx={{ pt: 5, justifyContent: 'center' }}>
        {gameList.games && gameList.games.map((game, key) => {
          return (
            <Grid item key={key}>
              {
                game.status === 'WAITING' ?
                  <Card sx={{ width: '23vw', height: '14vw' }} onClick={(e) => { roomSettingOpen(); fetchGameInfo(game.gameId, e); }}>
                    <CardHeader sx={{ p: 1, bgcolor: '#424242', color: '#DCD7C9', height: '1vw' }} subheader={
                      <Typography>
                        #{game.gameId}
                      </Typography>
                    }>
                    </CardHeader>
                    <CardContent>
                      <Typography sx={{ fontSize: 22 }} color={'#4F585B'}>
                        {game.title}
                      </Typography>
                    </CardContent>
                    <CardActions sx={{ pt: 9, pr: 3, display: 'flex', justifyContent: 'end' }}>
                      <Typography sx={{ pr: 1 }}>
                        {game.currentMembers}/{game.maxMember}
                      </Typography>
                      <Button variant="outlined" sx={btnStyle}>
                        입장
                      </Button>
                    </CardActions>
                  </Card>
                  :
                  <Card sx={{ width: '23vw', height: '14vw', position: 'relative' }}>
                    <Card sx={{ width: '23vw', height: '14vw', position: 'absolute', opacity: 0.8, bgcolor: "#282316" }}>
                      <Typography sx={{ pt: '20%', fontSize: 30, display: 'flex', justifyContent: 'center' }} color={'#DCD7C9'}>
                        게임 진행 중
                      </Typography>
                    </Card>
                    <CardHeader sx={{ p: 1, bgcolor: '#424242', color: '#DCD7C9', height: '1vw' }} subheader={
                      <Typography>
                        #{game.gameId}
                      </Typography>
                    }>
                    </CardHeader>
                    <CardContent>
                      <Typography sx={{ fontSize: 22 }} color={'#4F585B'}>
                        {game.title}
                      </Typography>
                    </CardContent>
                    <CardActions sx={{ pt: 9, pr: 3, display: 'flex', justifyContent: 'end' }}>
                      <Typography sx={{ pr: 1 }}>
                        {game.currentMembers}/{game.maxMember}
                      </Typography>
                      <Button variant="disabled" sx={nobtnStyle}>
                        입장불가
                      </Button>
                    </CardActions>
                  </Card>
              }
            </Grid>
          )
        }
        )}
        {/* {
        gameListRender(gameList)
        } */}
        <GameDescModal id={id} open={open} onClose={roomSettingClose}></GameDescModal>
      </Grid>
      <Pagination
        sx={{ pt: 2, pb: 2, display: 'flex', justifyContent: 'center' }}
        page={page}
        count={5}
        rowsPerPage={rowsPerPage}
        onChange={handleChangePage}
      >
      </Pagination>
    </div>
  )
}