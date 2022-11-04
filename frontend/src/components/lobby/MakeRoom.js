import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { makeRoom } from '../../_slice/roomSlice';
import EnterRoom from './EnterRoom';
// import Swal from 'sweetalert2'
// import withReactContent from 'sweetalert2-react-content'
// const MySwal = withReactContent(Swal);

export default function MakeRoom(){
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const roomInfo = useSelector(state => state.room.roomInfo);
  dispatch(makeRoom(roomInfo));
  const gameId = useSelector(state=>state.room.roomInfo.gameId); 
  navigate(`/room/${gameId}`);
  
  return (
    <div>방 생성중</div>
  );
}