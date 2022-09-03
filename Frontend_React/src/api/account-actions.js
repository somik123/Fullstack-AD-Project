import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios');
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

class AccountDataService {

  getUserByuserName = (userName) => {
    //return axios.get(`${BASE_URL}/user/username/${userName}`, { headers: { Authorization: AuthStr } })
    return axios.get(`${AppSetting.BASE_URL}/user/username/${userName}`, { headers: { Authorization: AuthStr } })
  };

  updateUser = (user) => {
    //return axios.put(`${BASE_URL}/{user.id}`, user, { headers: { Authorization: AuthStr } })
    return axios.put(`${AppSetting.BASE_URL}/user/${user.id}`, user, { headers: { Authorization: AuthStr } })
  };

  updatePassword = (passwordDTO) => {
    //return axios.put(`${BASE_URL}/{user.id}`, user, { headers: { Authorization: AuthStr } })
    return axios.put(`${AppSetting.BASE_URL}/user/updatePassword`, passwordDTO, { headers: { Authorization: AuthStr } })
  };

  searchUser = (userName) => {
    //return axios.get(`${BASE_URL}/user/username/${userName}`, { headers: { Authorization: AuthStr } })
    return axios.get(`${AppSetting.BASE_URL}/user/search/${userName}`, { headers: { Authorization: AuthStr } })
  };

  getAllInterest = () => {
    //return axios.get(`${BASE_URL}/user/username/${userName}`, { headers: { Authorization: AuthStr } })
    return axios.get(`${AppSetting.BASE_URL}/interest/all`, { headers: { Authorization: AuthStr } })
  };

  updateInterest = (interestDTO) => {
    //return axios.put(`${BASE_URL}/{user.id}`, user, { headers: { Authorization: AuthStr } })
    return axios.post(`${AppSetting.BASE_URL}/interest/edit`, interestDTO, { headers: { Authorization: AuthStr } })
  };
}

export default new AccountDataService();