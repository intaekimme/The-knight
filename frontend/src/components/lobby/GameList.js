import React from "react";
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";

import { Card, Grid, Pagination } from "@mui/material";
import RoomInfoModal from "../../commons/modal/RoomInfoModal";
import { gameDesc, gameListAll } from "../../_slice/tempGameSlice";
import { getRoomInfo } from "../../_slice/roomSlice";
import { Navigate } from "react-router-dom";

export default function GameList() {
  const dispatch = useDispatch();
	const navigate = useNavigate();

  const roomData = useSelector(state => state.room.roomInfo);
  const [isSetting, setIsSetting] = React.useState(false);
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
  const onRoomInfoModalOpen = () => setOpen(true);
  const onRoomInfoModalClose = () => setOpen(false);

  const [clickGameId, setClickGameId] = React.useState(-1);

  // 방 입장
  const onRoomEnter = (gameId)=>{
    navigate(`/room/${gameId}`);
  }

  // gameId props
  const [id, setId] = React.useState(0);
  const fetchGameInfo = (gameId, e) => {
    e.preventDefault();
    console.log("gameId", gameId);
    setId(gameId);
    //gameId로 게임 상세정보 불러오기
    // dispatch(gameDesc(gameId));
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

  return (
    <div>
      <Grid container spacing={3} columnSpacing={5} sx={{ pt: 5, justifyContent: 'center' }}>
        {gameList.games && gameList.games.map((game, key) => {
          return (
            <Grid item key={key}>
              <Card sx={{ width: '23vw', height: '14vw' }} onClick={(e) => { setClickGameId(game.gameId); fetchGameInfo(game.gameId, e); onRoomInfoModalOpen(); }}>
                {game.title}
              </Card>
            </Grid>
          )
        }
        )}
        {/* {
        gameListRender(gameList)
        } */}
        <RoomInfoModal canEdit={false} roomData={roomData} open={open} onClose={ onRoomInfoModalClose } onConfirm={ () => {onRoomEnter(clickGameId);}}/>
      </Grid>
      <Pagination
        sx={{ pt: 2, display: 'flex', justifyContent: 'center' }}
        page={page}
        count={5}
        rowsPerPage={rowsPerPage}
        onChange={handleChangePage}
      >
      </Pagination>
    </div>
  )
}