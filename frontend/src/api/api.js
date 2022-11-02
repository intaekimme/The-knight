const BASE_URL = 'http://localhost:8080';
// const API_BASE_URL = 'https://j7a301.p.ssafy.io';

const API = '/api';
const WEBSOCKET = '/pub';

const EXAMPLE = '/example';
const GOOGLE_LOGIN = '/oauth2/authorization/google';

const GAME = '/games';
const MEMBER_ALL = '/members';

const api = {
  exampleFunction: () => BASE_URL + EXAMPLE + `${0}`,
  login: () => BASE_URL + GOOGLE_LOGIN,
  gameRoomInfo: (gameId) => BASE_URL + API + GAME + `/${gameId}`,
  gameMemberAll: (gameId) => BASE_URL + WEBSOCKET + GAME + `/${gameId}` + MEMBER_ALL,
}
export default api;