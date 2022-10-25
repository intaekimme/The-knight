import React from "react";
import { useNavigate } from 'react-router-dom';
import LoginCheck from "../commons/login/LoginCheck";
import PlayerWithWeapon from '../components/game/playerWithWeapon';
import OrderPicker from '../components/game/orderPicker';
import WeaponPicker from '../components/game/weaponPicker';

import Grid from '@mui/material/Grid';

export default function Information() {
  // 비 로그인 시 로그인 화면으로
  const isLogin = LoginCheck();
  const navigate = useNavigate();
  React.useEffect(()=>{
    if(!isLogin){
      navigate('/login');
    }
  }, []);

  const dummyPlayers = [
    {
      userName: "player1",
      team: "A"
    },
    {
      userName: "player2",
      team: "A"
    },
    {
      userName: "player3",
      team: "A"
    },
    {
      userName: "player4",
      team: "B"
    },
    {
      userName: "player5",
      team: "B"
    },
    {
      userName: "player6",
      team: "B"
    },
  ]

  function placePlayers(players) {
    let arr = [];
    for (let i = 0; i < players.length; i++) {
      // A를 추후에 로그인한 유저의 팀과 비교하도록 변경
      if (players[i].team === "A") {
        arr.push(
          // 5대5일 경우 추가
          <Grid item xs={12 / (players.length / 2)} key={i}>
            {/* 추후에 isMe 삭제 */}
            <PlayerWithWeapon isMe={true} nickName={players[i].userName} />
          </Grid>
        )
      }
    }
    return arr;
  }

  return (
    <div>
      <h1>진행 순서와 무기를 선택하세요</h1>
      <h3>100</h3>
      <div style={{ backgroundColor: "grey" }}>
        당신은 리더입니다.
      </div>
      <OrderPicker dummyPlayers={dummyPlayers} />
      <WeaponPicker></WeaponPicker>
      <Grid container>
        { placePlayers(dummyPlayers) }
      </Grid>
    </div>
  );
}