import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import api from '../../api/api';
import EnterRoom from './EnterRoom';
// import Swal from 'sweetalert2'
// import withReactContent from 'sweetalert2-react-content'
// const MySwal = withReactContent(Swal);

export default function MakeRoom(roomInfo){
  return axios.post(`${api.makeRoom()}`, JSON.stringify(roomInfo), {
    headers: {Authorization: `Bearer ${window.localStorage.getItem("loginToken")}`}
  }).then((response) => {
    console.log("방 생성 성공", response);
    EnterRoom(response.gameId);
    return response.gameId;
  })
  .catch((error) => {
    // if (error.response.status === 401) {
    //   const refresh = apiClient.refreshAccessToken();
    // }

    // MySwal.fire({
    //   icon: 'error',
    //   title: '피드 수정 실패..',
    //   text: '다시 시도해주세요',
    //   confirmButtonColor: '#66cc66',
    // });
    console.log(error);
    console.log("방 생성 실패", error);
    return -1;
  });
}