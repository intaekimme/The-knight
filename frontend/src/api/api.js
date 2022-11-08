//backend Local
// const BASE_URL = 'http://localhost:8080';
//backend 배포
const BASE_URL = 'https://sword-shield.co.kr';

//frontend Local
// const LOGIN_REDIRECT = 'http://localhost:3000/islogin';
//frontend 배포
const LOGIN_REDIRECT = 'https://sword-shield.co.kr/islogin';

const API = '/api';
const WEBSOCKET = '/websocket';
const WEBSOCKET_PUBLISH = '/pub';
const WEBSOCKET_SUBSCRIBE = '/sub';

const EXAMPLE = '/example';
const GOOGLE_LOGIN = '/oauth2/authorization/google';

const GAME = '/games';

// 대기방
const CHAT = '/chat';
const MODIFYSETTING = '/modify';
const ENTER_ROOM = '/entry';
const ALL_MEMBERS = '/members';
const EXIT_ROOM = '/exit';
const SELECT_TEAM = '/team';
const READY = '/ready';

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
  subModifyRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + MODIFYSETTING,
  subState: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}`,
  subChatAll: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CHAT + `-all`,
  subChatTeam: (gameId, team) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CHAT + `-${team}`,
  subEnterRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ENTER_ROOM,
  subAllMembersInRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ALL_MEMBERS,
  subSelectTeam: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + SELECT_TEAM,
  subReady: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + READY,
  subExitRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + EXIT_ROOM,

  // 발행
  pubModifyRoom: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + MODIFYSETTING,
  pubChat: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CHAT,
  // chatTeam: (gameId, team) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CHAT + `${team}`,
  pubEnterRoom: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + ENTER_ROOM,
  pubAllMembersInRoom: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + ALL_MEMBERS,
  pubSelectTeam: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + SELECT_TEAM,
  pubReady: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + READY,
  pubExitRoom: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + EXIT_ROOM,

  gameRoomInfo: (gameId) => BASE_URL + API + GAME + `/${gameId}`,
  getGameList: () => BASE_URL + API + GAME,

  //memebr 관련
  getMemberHistory: () => BASE_URL + API + '/history',
  getMemberInfo: () => BASE_URL + API + ALL_MEMBERS,
  deleteMember: () => BASE_URL + API + ALL_MEMBERS,
  updateMemberInfo: () => BASE_URL + API + ALL_MEMBERS,

  getAllPalyers: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ALL_PLAYERS,
  goLoading: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CONVERT,
  readyForNextPhase: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CONVERT_COMPLETE,
  nextPhase: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + PROCEED,
}
export default api;