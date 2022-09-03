import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios');
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

class ChatDataService {
    getChatHistory = () => {
        // return axios.get(`${BASE_URL}/group/user/${Claims.getUserId()}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/message/individual/chathistory/${Claims.getUserId()}`, { headers: { Authorization: AuthStr } });
    };

    getMessages = (receiverId) => {
        return axios.get(`${AppSetting.BASE_URL}/message/individual/${Claims.getUserId()}/receiver/${receiverId}`, { headers: { Authorization: AuthStr } });
    };

    sendMessage = (messageRequest) => {
        return axios.post(AppSetting.BASE_URL + "/message/individual/create", messageRequest, { headers: { Authorization: AuthStr } });
    };
}

export default new ChatDataService();