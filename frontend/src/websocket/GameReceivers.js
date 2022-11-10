import { useDispatch, useSelector } from "react-redux";
import { fetchPlayers } from "../_slice/gameSlice";
import api from "../api/api";

const dispatch = useDispatch();
const stompClient = useSelector(state => state.websocket.stompClient);
const myTeam = useSelector(state => state.game.me).team

////////////////////////////////////////////////
//////////////// Common(Connect) ///////////////
////////////////////////////////////////////////

// 전체 플레이어 정보
const onSubPlayersInfo = (payload) => {
  // {
  //   state : String : 현재 게임진행 차례
  //   maxMember : int (2 vs 2 → 4),
  //   players : [
  //     {
  //       memberId : long,
  //       nickname : String,
  //       team: String,
  //       leftCount : int,
  //       rightCount : int,
  //       order : int, (none: 0)
  //       weapons : []
  //     },
  //   ]
  // }
  const data = JSON.parse(payload.body)
  dispatch(fetchPlayers(data))
}

// 최초 화면전환 요청
const onSubConvert = (payload) => {
  // {
  //   gameStatus : String,
  // }
  const nextPhase = JSON.parse(payload.body).gameStatus;
  // 화면전환을 위한 구독 준비
  if (nextPhase === "PREPARE") {
    // for prepare
    stompClient.subscribe(api.subLeader(gameId, myTeam), onSubLeader)
    stompClient.subscribe(api.subCountWeapon(gameId, myTeam), onSubCountWeapon)
    stompClient.subscribe(api.subOrder(gameId, myTeam), onSubOrder)
    stompClient.subscribe(api.subSelectComplete(gameId, myTeam), onSubSelectComplete)
  } else if (nextPhase === "PREDECESSOR") {
    stompClient.subscribe(api.subAttackFirst(gameId), onSubAttackFirst)
  } else if (nextPhase === "ATTACK") {
    stompClient.subscribe(api.subCurrentAttacker(gameId, myTeam), onSubCurrentAttacker)
  } else if (nextPhase === "ATTACK_DOUBT") {
    stompClient.subscribe(api.subAttackInfo(gameId), onSubAttackInfo)
  } else if (nextPhase === "DEFENSE") {

  } else if (nextPhase === "DEFENSE_DOUBT") {
    stompClient.subscribe(api.subDefenseInfo(gameId), onSubDefenseInfo)
  } else if (nextPhase === "DOUBT_RESULT") {
    stompClient.subscribe(api.subDoubtInfo(gameId), onSubDoubtInfo)
  } else if (nextPhase === "EXECUTE") {
    stompClient.subscribe(api.subExecute(gameId), onSubExecute)
  } else if (nextPhase === "END") {

  } 
  // 준비완료 publish
}

// 실제 화면전환
const onSubNextPhase = () => {
  // phase 변경
}

// 제한시간 
const onSubTimer = () => {
  // Redux에 제한시간 저장
  // 1초씩 차감
}

// 게임결과
const onSubEnd = () => {

}

////////////////////////////////////////////////
/////////////////// PREPARE ////////////////////
////////////////////////////////////////////////

// 팀 리더
const onSubLeader = () => {
  // Redux에 각 팀 리더 저장
}

// 팀 남은 무기 개수
const onSubCountWeapon = () => {

}

// 팀 현재까지 선택된 순서
const onSubOrder = () => {

}

// 팀 무기&순서 선택완료
const onSubSelectComplete = () => {

}

////////////////////////////////////////////////
///////////////// PREDECESSOR //////////////////
////////////////////////////////////////////////

// 선공 팀
const onSubAttackFirst = () => {
  // Redux에 선공 팀 저장
}

////////////////////////////////////////////////
/////////////////// ATTACK /////////////////////
////////////////////////////////////////////////

// 현재 공격자
const onSubCurrentAttacker = () => {
  // Redux에 공격자 저장
}

////////////////////////////////////////////////
///////////////// Attack Doubt /////////////////
////////////////////////////////////////////////

// 공격정보
const onSubAttackInfo = () => {
  // 공격정보 저장해서 doubt에서 띄워주기
  // 현재 방어자 갱신
}

////////////////////////////////////////////////
/////////////////// Defense ////////////////////
////////////////////////////////////////////////


////////////////////////////////////////////////
///////////////// Defense Doubt ////////////////
////////////////////////////////////////////////

// 방어정보
const onSubDefenseInfo = () => {
  // 방어정보 갱신해서 doubt에 띄우기
}


////////////////////////////////////////////////
///////////////// Doubt Result /////////////////
////////////////////////////////////////////////

// 의심정보
const onSubDoubtInfo = () => {

}

////////////////////////////////////////////////
/////////////////// Execute ////////////////////
////////////////////////////////////////////////

// 애니메이션 수행 정보
const onSubExecute = () => {

}