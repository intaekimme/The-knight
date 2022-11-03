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
const MEMBERS_GAMEINFO = '/members';
const MEMBERS_ROOMINFO = '/members-room';

const ENTER_ROOM = '/entry';

const api = {
  exampleFunction: () => BASE_URL + EXAMPLE + `${0}`,
  baseURL: () => BASE_URL,
  websocket: () => BASE_URL + WEBSOCKET,
  login: () => BASE_URL + GOOGLE_LOGIN,
  loginRedirect: () => LOGIN_REDIRECT,
  makeRoom: () => BASE_URL + API + GAME,
  enterRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ENTER_ROOM,
  gameRoomInfo: (gameId) => BASE_URL + API + GAME + `/${gameId}`,
  gameMembersInfo: (gameId) => BASE_URL + WEBSOCKET_PUBLISH + GAME + `/${gameId}` + MEMBERS_ROOMINFO,
}
export default api;