import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios')
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*'

class EventDataService {

  getAllEvents = () => {
    //return axios.get(BASE_URL + "/event/all",{ headers: { Authorization: AuthStr } })
    return axios.get(AppSetting.BASE_URL + "/event/all", { headers: { Authorization: AuthStr } })
  };

  getEventById = (id) => {
    //return axios.get(`${BASE_URL}/event/${id}`, { headers: { Authorization: AuthStr } })
    return axios.get(`${AppSetting.BASE_URL}/event/${id}`, { headers: { Authorization: AuthStr } })
  };

  getEventByName = (name) => {
    //return axios.get(`${BASE_URL}/event/name/${name}`, { headers: { Authorization: AuthStr } })
    return axios.get(`${AppSetting.BASE_URL}/event/name/${name}`, { headers: { Authorization: AuthStr } })
  };

  createEvent = (event) => {
    //return axios.post(`${BASE_URL}/event`, event, { headers: { Authorization: AuthStr } })
    return axios.post(`${AppSetting.BASE_URL}/event/`, event, { headers: { Authorization: AuthStr } })
  };

  joinEvent = (eventId, userId) => {
    //return axios.post(`${BASE_URL}/event`, event, { headers: { Authorization: AuthStr } })
    return axios.post(`${AppSetting.BASE_URL}/event/${userId}/${eventId}`, null, { headers: { Authorization: AuthStr } })
  };
}

export default new EventDataService()