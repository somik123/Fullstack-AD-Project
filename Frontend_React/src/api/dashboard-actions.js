import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios');
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

class DashboardDataService {

    getMessagesPerWeek = () => {
        // return axios.get(`${BASE_URL}/group/user/${Claims.getUserId()}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/message/detail/${Claims.getUserId()}/days/7`, { headers: { Authorization: AuthStr } })
    };

    getTotalMessgaeSend = () => {
        // return axios.get(`${BASE_URL}/group/user/${Claims.getUserId()}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/message/${Claims.getUserId()}/days/7`, { headers: { Authorization: AuthStr } })
    };

    getDashboardData = () => {
        // return axios.get(`${BASE_URL}/group/user/${Claims.getUserId()}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/dashboard/${Claims.getUserName()}`, { headers: { Authorization: AuthStr } })
    };
}

export default new DashboardDataService();