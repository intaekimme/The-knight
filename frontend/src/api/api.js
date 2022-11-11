//backend Local
// const BASE_URL = 'http://localhost:8080';
//backend 배포
const BASE_URL = 'https://sword-shield.co.kr';

//frontend Local
const LOGIN_REDIRECT = 'http://localhost:3000/islogin';
//frontend 배포
// const LOGIN_REDIRECT = 'https://sword-shield.co.kr/islogin';

const API = '/api';
const WEBSOCKET = '/websocket';
const WEBSOCKET_PUBLISH = '/pub';
const WEBSOCKET_SUBSCRIBE = '/sub';

const EXAMPLE = '/example';
const GOOGLE_LOGIN = '/oauth2/authorization/google';

const GAME = '/games';

// 대기방
const MODIFY = '/modify';
const CHAT = '/chat';
const ENTRY = '/entry';
const MEMBERS = '/members';
const TEAM = '/team';
const READY = '/ready';
const EXIT = '/exit';

// 인게임
const ALL_PLAYERS = '/players';
const CONVERT = '/convert';
const CONVERT_COMPLETE = '/convert-complete';
const PROCEED = '/proceed';

const api = {
  exampleFunction: () => BASE_URL + EXAMPLE + `${0}`,
  baseURL: () => BASE_URL,
  websocket: () => BASE_URL + WEBSOCKET,
  login: () => BASE_URL + GOOGLE_LOGIN,
  loginRedirect: () => LOGIN_REDIRECT,


  initRoom: () => BASE_URL + API + GAME,

  // 구독
  subModifyRoom: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + MODIFY,
  subState: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}`,
  subChatAll: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CHAT + `-all`,
  subChatTeam: (gameId, team) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CHAT + `-${team}`,
  subEntry: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ENTRY,
  subMembers: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + MEMBERS,
  subSelectTeam: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + TEAM,
  subReady: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + READY,
  subExit: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + EXIT,

  // 발행
  pubModifyRoom: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + MODIFY,
  pubChat: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CHAT,
  // chatTeam: (gameId, team) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CHAT + `${team}`,
  pubEntry: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + ENTRY,
  pubMembers: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + MEMBERS,
  pubSelectTeam: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + TEAM,
  pubReady: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + READY,
  pubExit: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + EXIT,

  gameRoomInfo: (gameId) => BASE_URL + API + GAME + `/${gameId}`,
  getGameList: () => BASE_URL + API + GAME,

  //memebr 관련
  getMemberHistory: () => BASE_URL + API + '/history',
  getMemberInfo: () => BASE_URL + API + MEMBERS,
  deleteMember: () => BASE_URL + API + MEMBERS,
  updateMemberInfo: () => BASE_URL + API + MEMBERS,

  getAllPalyers: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ALL_PLAYERS,
  goLoading: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CONVERT,
  readyForNextPhase: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CONVERT_COMPLETE,
  nextPhase: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + PROCEED,

  //rank 관련
  getRankList: () => BASE_URL + API + '/ranking',
}
export default api;