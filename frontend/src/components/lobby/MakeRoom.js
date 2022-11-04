import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { initRoom } from '../../_slice/roomSlice';
import EnterRoom from './EnterRoom';
// import Swal from 'sweetalert2'
// import withReactContent from 'sweetalert2-react-content'
// const MySwal = withReactContent(Swal);

export default function MakeRoom(){
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const roomInfo = useSelector(state => state.room.roomInfo);
  const url = `/room/`;
  const [isMake, setIsMake] = React.useState(false);
  React.useEffect(()=>{
    if(!isMake){
      setIsMake(true);
      dispatch(initRoom({navigate:navigate, url:url, roomInfo:roomInfo}));
    }
  }, []);
  
  return (
    <div>방 생성중</div>
  );
}