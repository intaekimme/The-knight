import { useDispatch, useSelector } from "react-redux";
import {
  setTimer,
  countTimer,
  stopCountTimer,
  fetchPlayers,
  fetchOrder,
  fetchPhase,
  setEndInfo,
  setLeader,
  setAttackFirst,
  fetchCountWeapon,
  selectComplete,
  fetchCurrentAttacker,
  fetchCurrentDefenser,
  fetchAttackInfo,
  fetchDefenseInfo,
} from "../_slice/gameSlice";
import api from "../api/api";

const dispatch = useDispatch();
const stompClient = useSelector((state) => state.websocket.stompClient);
const myTeam = useSelector((state) => state.game.me).team;

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
  const data = JSON.parse(payload.body);
  dispatch(fetchPlayers(data));
};

// 최초 화면전환 요청
const onSubConvert = (payload) => {
  // {
  //   gameStatus : String,
  // }
  const data = JSON.parse(payload.body);
  const nextPhase = data.gameStatus;
  // 화면전환을 위한 구독 준비
  if (nextPhase === "PREPARE") {
    // for prepare
    stompClient.subscribe(api.subLeader(gameId, myTeam), onSubLeader);
    stompClient.subscribe(api.subCountWeapon(gameId, myTeam), onSubCountWeapon);
    stompClient.subscribe(api.subOrder(gameId, myTeam), onSubOrder);
    stompClient.subscribe(api.subSelectComplete(gameId, myTeam), onSubSelectComplete);
  } else if (nextPhase === "PREDECESSOR") {
    stompClient.subscribe(api.subAttackFirst(gameId), onSubAttackFirst);
  } else if (nextPhase === "ATTACK") {
    stompClient.subscribe(api.subCurrentAttacker(gameId, myTeam), onSubCurrentAttacker);
  } else if (nextPhase === "ATTACK_DOUBT") {
    stompClient.subscribe(api.subAttackInfo(gameId), onSubAttackInfo);
  } else if (nextPhase === "DEFENSE") {
  } else if (nextPhase === "DEFENSE_DOUBT") {
    stompClient.subscribe(api.subDefenseInfo(gameId), onSubDefenseInfo);
  } else if (nextPhase === "DOUBT_RESULT") {
    stompClient.subscribe(api.subDoubtInfo(gameId), onSubDoubtInfo);
  } else if (nextPhase === "EXECUTE") {
    stompClient.subscribe(api.subExecute(gameId), onSubExecute);
  } else if (nextPhase === "END") {
  }

  // 준비완료 sub/pub
  stompClient.subscribe(api.subConvertComplete(gameId), onSubConvertComplete);
  stompClient.send(api.pubConvertComplete(gameId), {}, {});
};

// 다음 phase에 필요한 정보 요청
const onSubConvertComplete = (payload) => {
  // {
  //   postfix: String,
  // }
  const data = JSON.parse(payload.body);
  stompClient.send(api.pubPostfix(gameId, data.posfix), {}, {});
};

// 실제 화면전환
const onSubNextPhase = (payload) => {
  // {
  //   gameStatus : String,
  // }
  const data = JSON.parse(payload.body);
  dispatch(fetchPhase(data.gameStatus));
};

// 제한시간 (변경사항이 많을 것으로 예상)
const onSubTimer = (payload) => {
  // {
  //   remainTime : int
  // }
  const data = JSON.parse(payload.body);
  dispatch(setTimer(data.remainTime));

  const intervalObject = setInterval(() => {
    // 1초씩 감소
    dispatch(countTimer(intervalObject));
    // 0 이면 카운트 중단
    dispatch(stopCountTimer());
  }, 1000);
};

// 게임결과
const onSubEnd = (payload) => {
  // {
  //   isWin : boolean,
  //   losingTeam : String (A, B),
  //   losingLeaderId : long,
  //   winningLeaderId : long,
  //   players : [
  //     memberId : long,
  //     leftWeapon :
  //     rightWeapon :
  //   ], ...
  // }
  const data = JSON.parse(payload.body);
  dispatch(setEndInfo(data));
};

////////////////////////////////////////////////
/////////////////// PREPARE ////////////////////
////////////////////////////////////////////////

// 팀 리더
const onSubLeader = (payload) => {
  // {
  //   memberId : long
  // }
  const data = JSON.parse(payload.body);
  dispatch(setLeader(data.memberId));
};

// 팀 남은 무기 개수
const onSubCountWeapon = (payload) => {
  // {
  //   sword : int,
  //   twin : int,
  //   shield : int,
  //   hand : int
  // }
  const data = JSON.parse(payload.body);
  dispatch(fetchCountWeapon(data));
};

// 팀 현재까지 선택된 순서
const onSubOrder = (payload) => {
  // {
  //   orderList : [
  //       memberId : long,
  //       nickname : String,
  //       image: String
  //   ]
  // }
  const data = JSON.parse(payload.body);
  dispatch(fetchOrder(data.orderList));
};

// 팀 무기&순서 선택완료
const onSubSelectComplete = (payload) => {
  // {
  //   selectCompleted: boolean
  // }
  const data = JSON.parse(payload.body);
  if (data.selectCompleted) {
    dispatch(selectComplete());
  }
};

////////////////////////////////////////////////
///////////////// PREDECESSOR //////////////////
////////////////////////////////////////////////

// 선공 팀
const onSubAttackFirst = (payload) => {
  // {
  //   preAttackTeam : String (A, B),
  // }
  const data = JSON.parse(payload.body);
  dispatch(setAttackFirst(data.preAttackTeam));
};

////////////////////////////////////////////////
/////////////////// ATTACK /////////////////////
////////////////////////////////////////////////

// 현재 공격자
const onSubCurrentAttacker = (payload) => {
  // {
  //   memberId : long,
  //   team: String,
  // }
  const data = JSON.parse(payload.body);
  dispatch(fetchCurrentAttacker(data))
};

// 공격패스

////////////////////////////////////////////////
///////////////// Attack Doubt /////////////////
////////////////////////////////////////////////

// 공격정보
const onSubAttackInfo = (payload) => {
  // {
  //   attacker : {
  //     memberId: long : 해당 유저가 공격을 진행할 것이다,
  //     team: String
  //   }
  //   defenser : {
  //     memberId: long : 공격 대상자(=다음 방어자),
  //     team: String
  //   }
  //   weapon : String : 사용 선언무기
  //    (SWORD, TWIN, SHEILD, HAND, HIDE)
  //   hand : String (LEFT, RIGHT)
  // }
  const data = JSON.parse(payload.body);
  dispatch(fetchCurrentDefenser(data.defenser))
  dispatch(fetchAttackInfo(data))
};

////////////////////////////////////////////////
/////////////////// Defense ////////////////////
////////////////////////////////////////////////

// 방어패스

////////////////////////////////////////////////
///////////////// Defense Doubt ////////////////
////////////////////////////////////////////////

// 방어정보
const onSubDefenseInfo = (payload) => {
  // {
  //   defenser : {
  //     memberId: long : 방어자,
  //   }
  //   weapon : String : 사용 선언무기
  //     (SWORD, TWIN, SHEILD, HAND, HIDE)
  //   hand : String (LEFT, RIGHT)
  // }
  const data = JSON.parse(payload.body);
  dispatch(fetchDefenseInfo(data));
};

////////////////////////////////////////////////
///////////////// Doubt Result /////////////////
////////////////////////////////////////////////

// 의심정보
const onSubDoubtInfo = () => {};

////////////////////////////////////////////////
/////////////////// Execute ////////////////////
////////////////////////////////////////////////

// 애니메이션 수행 정보
const onSubExecute = () => {};
