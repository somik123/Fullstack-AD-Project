import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios');
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

class FileDataService {

    getFile = (fileName) => {
        // return axios.get(`${BASE_URL}/group/user/${Claims.getUserId()}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.FILE_UPLOAD_URL}/${fileName}`, { headers: { Authorization: AuthStr } })

    };

    getListFiles = () => {
        // return axios.get(`${BASE_URL}/message/group/${groupId}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.FILE_UPLOAD_URL}`, { headers: { Authorization: AuthStr } })
    };

    uploadFile = (fileRequest) => {
        return axios.post(AppSetting.FILE_UPLOAD_URL, fileRequest, { headers: { 'Content-Type': 'multipart/form-data', Authorization: AuthStr } })
    };
}

export default new FileDataService();