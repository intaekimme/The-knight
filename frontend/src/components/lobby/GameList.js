import React from "react";
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import api from "../../api/api";
import RoomInfoModal from "../../commons/modal/RoomInfoModal";
import { gameDesc, gameListAll } from "../../_slice/tempGameSlice";
import { getRoomInfo } from "../../_slice/roomSlice";

import styled from 'styled-components';
import { Button, Card, CardActions, CardContent, CardHeader, Grid, Pagination, PaginationItem, Typography } from "@mui/material";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';

const RoomStyle = styled.div`
  .css-1r4fj62-MuiPaper-root-MuiCard-root:hover {
    color: #424242 !important;
    background-color: #E9E9E9;
    box-shadow: 2px 2px 25px #424242;
    transition-duration: 0.2s;
  }
  > a:visited {
    color: black;
    text-decoration: none;
  }
`;

export default function GameList() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const roomData = useSelector(state => state.room.roomInfo);
  const [isSetting, setIsSetting] = React.useState(false);
  React.useEffect(() => {
    if (!isSetting) {
      setIsSetting(true);
      dispatch(gameListAll(0));
    }
  }, []);
  //temp data
  // const gameList = useSelector(state => state.tempGame.value.gameList)

  //real data
  const gameList = useSelector(state => state.tempGame.gameListAll)
  console.log("gmaelistall", gameList);


  // 방설정 모달
  const [open, setOpen] = React.useState(false);
  const onRoomInfoModalOpen = () => setOpen(true);
  const onRoomInfoModalClose = () => setOpen(false);

  const [clickGameId, setClickGameId] = React.useState(-1);

  // 방 입장
  const onRoomEnter = (gameId) => {
    dispatch(getRoomInfo(gameId)).then((response) => {
      if (response.payload.currentMembers < response.payload.maxMember) {
        onRoomInfoModalClose();
        navigate(api.routeConnectWebsocket(gameId));
      }
      else {
        console.log("full Room");
      }
    }).catch((err) => { console.log(err); });
  }

  // gameId props
  const [id, setId] = React.useState(0);
  const fetchGameInfo = (gameId, e) => {
    e.preventDefault();
    console.log("gameId", gameId);
    setId(gameId);
    //gameId로 게임 상세정보 불러오기
    dispatch(getRoomInfo(gameId));
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
  //           <Card sx={{ width: '23vw', height: '14vw' }} onClick={() => { onRoomInfoModalOpen(); fetchGameInfo(gameList[index].gameId); }}>
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
      color: '#fff',
      bgcolor: '#424242',
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
                  <RoomStyle>
                    <Card sx={{ width: '23vw', height: '14vw', opacity: 0.8, borderRadius: 1.5 }} onClick={(e) => { setClickGameId(game.gameId); fetchGameInfo(game.gameId, e); onRoomInfoModalOpen(); }}>
                      <CardHeader sx={{ p: 1, bgcolor: '#424242', color: '#DCD7C9', height: '1vw' }} subheader={
                        <Typography>
                          #{game.gameId}
                        </Typography>
                      }>
                      </CardHeader>
                      <CardContent>
                        <Typography sx={{ fontSize: 22, fontWeight: 'bold' }} color={'#4F585B'}>
                          {game.title}
                        </Typography>
                      </CardContent>
                      <CardActions sx={{ pt: 7.5, pr: 3, display: 'flex', justifyContent: 'end' }}>
                        <Typography sx={{ pr: 1 }}>
                          {game.currentMembers}/{game.maxMember}
                        </Typography>
                        <Button variant="outlined" sx={btnStyle}>
                          입장
                        </Button>
                      </CardActions>
                    </Card>
                  </RoomStyle>
                  :
                  <RoomStyle>
                    <Card sx={{ width: '23vw', height: '14vw', position: 'relative', borderRadius: 1.5 }}>
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
                      <CardActions sx={{ pt: 7.5, pr: 3, display: 'flex', justifyContent: 'end' }}>
                        <Typography sx={{ pr: 1 }}>
                          {game.currentMembers}/{game.maxMember}
                        </Typography>
                        <Button variant="disabled" sx={nobtnStyle}>
                          입장불가
                        </Button>
                      </CardActions>
                    </Card>
                  </RoomStyle>
              }
            </Grid>
          )
        }
        )}
        {/* {
        gameListRender(gameList)
        } */}
        <RoomInfoModal canEdit={false} roomData={roomData} open={open} onClose={onRoomInfoModalClose} onConfirm={() => { onRoomEnter(clickGameId); }} />
      </Grid>
      <Pagination
        sx={{ pt: 7, pb: 2, display: 'flex', justifyContent: 'center' }}
        shape="rounded"
        page={page}
        count={gameList.maxPageNum}
        rowsperpage={rowsPerPage}
        onChange={handleChangePage}
        renderItem={(item) => (
          <PaginationItem
            sx={{ fontSize: 16, color: '#fff' }}
            slots={{ previous: ArrowBackIcon, next: ArrowForwardIcon }}
            {...item}
          />
        )}
      />
    </div>
  )
}