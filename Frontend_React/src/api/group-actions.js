import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios');
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

class GroupDataService {

    getGroups = () => {
        // return axios.get(`${BASE_URL}/group/user/${Claims.getUserId()}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/group/user/${Claims.getUserId()}`, { headers: { Authorization: AuthStr } });
    };

    getMessages = (groupId) => {
        // return axios.get(`${BASE_URL}/message/group/${groupId}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/message/group/${groupId}`, { headers: { Authorization: AuthStr } });
    };

    sendMessage = (messageRequest) => {
        return axios.post(AppSetting.BASE_URL + "/message/create", messageRequest, { headers: { Authorization: AuthStr } });
    };

    createPrivateGroup = (userId, participantId) => {
        return axios.post(`${AppSetting.BASE_URL}/group/individual/${userId}/${participantId}`, null, { headers: { Authorization: AuthStr } });
    };
}

export default new GroupDataService();