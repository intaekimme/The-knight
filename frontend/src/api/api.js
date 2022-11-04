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

  makeRoom: () => BASE_URL + API + GAME,
  enterRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ENTER_ROOM,
  allMembersInRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ALL_MEMBERS,
  exitRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + EXIT_ROOM,
  selectTeam: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + SELECT_TEAM,
  ready: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + READY,
  
  gameRoomInfo: (gameId) => BASE_URL + API + GAME + `/${gameId}`,

  getAllPalyers: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ALL_PLAYERS,
  goLoading: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CONVERT,
  readyForNextPhase: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CONVERT_COMPLETE,
  nextPhase: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + PROCEED,
}
export default api;