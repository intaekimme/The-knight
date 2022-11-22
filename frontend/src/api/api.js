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
const ERROR = '/error';

// 대기방
const MODIFY = '/modify';
const CHAT = '/chat';
const ENTRY = '/entry';
const MEMBERS = '/members';
const TEAM = '/team';
const READY = '/ready';
const EXIT = '/exit';
const DELETE = '/delete';

// 인게임
const PLAYERS_INFO = '/players';
const CONVERT = '/convert';
const CONVERT_COMPLETE = '/convert-complete';
const PROCEED = '/proceed';
const LEADER = '/leader';
const COUNT_WEAPON = '/weapons';
const SELECT_WEAPON = '/weapon-choice';
const DELETE_WEAPON = '/weapon-delete';
const TIMER = '/timer';
const ORDER = '/orders';
const SELECT_COMPLETE = '/select-complete';
const ATTACK_FIRST = '/pre-attack';
const CURRENT_ATTACKER = '/attacker'
const ATTACK = '/attack'
const DEFENSE = '/defense'
const ATTACK_INFO = '/attack-info'
const DEFENSE_INFO = '/defense-info'
const ATTACK_PASS = '/attack-pass'
const DEFENSE_PASS = '/defense-pass'
const DOUBT = '/doubt'
const DOUBT_INFO = '/doubt-info'
const EXECUTE = '/execute'
const END = '/end'
const DOUBT_PASS = '/doubt-pass'
const SCREEN_DATA = '/screen-data'

const api = {
  exampleFunction: () => BASE_URL + EXAMPLE + `${0}`,
  baseURL: () => BASE_URL,
  websocket: () => BASE_URL + WEBSOCKET,
  login: () => BASE_URL + GOOGLE_LOGIN,
  loginRedirect: () => LOGIN_REDIRECT,

  // react route dom navigate
  routeConnectWebsocket: (gameId) => `/connect-websocket/${gameId}`,
  routeEntryRoomSetting: (gameId) => `/entryRoom/${gameId}`,
  routeRoom: (gameId) => `/room/${gameId}`,

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
  subDelete: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + DELETE,
  subError: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ERROR,

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

  // 인게임
  subPlayersInfo: (gameId, team) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + `/${team}`.toLowerCase() + PLAYERS_INFO,
  subConvert: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CONVERT,
  subNextPhase: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + PROCEED,
  subTimer: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + TIMER,
  subLeader: (gameId, team) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + `/${team}`.toLowerCase() + LEADER,
  subCountWeapon: (gameId, team) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + `/${team}`.toLowerCase() + COUNT_WEAPON,
  subOrder: (gameId, team) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + `/${team}`.toLowerCase() + ORDER,
  subSelectComplete: (gameId, team) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + `/${team}`.toLowerCase() + SELECT_COMPLETE,
  subAttackFirst: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ATTACK_FIRST,
  subCurrentAttacker: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CURRENT_ATTACKER,
  subAttackInfo: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + ATTACK_INFO,
  subDefenseInfo: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + DEFENSE_INFO,
  subDoubtInfo: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + DOUBT_INFO,
  subExecute: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + EXECUTE,
  subEnd: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + END,
  subConvertComplete: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + CONVERT_COMPLETE,
  subDoubtPass: (gameId) => WEBSOCKET_SUBSCRIBE + GAME + `/${gameId}` + DOUBT_PASS,

  pubConvertComplete: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + CONVERT_COMPLETE,
  pubScreenData: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + SCREEN_DATA,
  pubSelectWeapon: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + SELECT_WEAPON,
  pubDeleteWeapon: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + DELETE_WEAPON,
  pubSelectComplete: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + SELECT_COMPLETE,
  pubAttack: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + ATTACK,
  pubDefense: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + DEFENSE,
  pubAttackPass: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + ATTACK_PASS,
  pubDefensePass: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + DEFENSE_PASS,
  pubDoubt: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + DOUBT,
  pubOrder: (gameId, team) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + `/${team}`.toLowerCase() + ORDER,
  pubDoubtPass: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + DOUBT_PASS,
  pubEnd: (gameId) => WEBSOCKET_PUBLISH + GAME + `/${gameId}` + END,

  //rank 관련
  getRankList: () => BASE_URL + API + '/ranking',
}
export default api;