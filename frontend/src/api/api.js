const BASE_URL = 'http://localhost:8080';
// const API_BASE_URL = 'https://j7a301.p.ssafy.io';

const API = '/api';
const WEBSOCKET_REQUEST = '/pub';
const WEBSOCKET_SUBSCRIBE = '/sub';

const EXAMPLE = '/example';
const GOOGLE_LOGIN = '/oauth2/authorization/google';

const GAME = '/games';
const MEMBERS_GAMEINFO = '/members';
const MEMBERS_ROOMINFO = '/members-room';

const ENTER_ROOM = '/entry';

const api = {
  exampleFunction: () => BASE_URL + EXAMPLE + `${0}`,
  login: () => BASE_URL + GOOGLE_LOGIN,
  enterRoom: (gameId) => BASE_URL + WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ENTER_ROOM,
  gameRoomInfo: (gameId) => BASE_URL + API + GAME + `/${gameId}`,
  gameMembersInfo: (gameId) => BASE_URL + WEBSOCKET_REQUEST + GAME + `/${gameId}` + MEMBERS_ROOMINFO,
}
export default api;