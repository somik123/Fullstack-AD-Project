import { AppSetting } from '../utils/AppSetting'
const axios = require('axios')
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*'

class CommonDataService {
    authenticate = (authRequest) => {
        return axios.post(AppSetting.BASE_URL + "/token/authenticate", authRequest)
    };

    register = (user) => {
        return axios.post(AppSetting.BASE_URL + "/user/register", user)
    };
}

export default new CommonDataService()