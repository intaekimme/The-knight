const API_BASE_URL = 'http://localhost:8081/api';
// const API_BASE_URL = 'https://j7a301.p.ssafy.io/api';

const EXAMPLE = '/example';
const GOOGLE_LOGIN = '/oauth2/authorization/google';

const api = {
  exampleFunction: () => API_BASE_URL + EXAMPLE + `${0}`,
  login: () => API_BASE_URL + GOOGLE_LOGIN,
}
export default api;