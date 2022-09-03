import { Claims } from '../utils/ClientCache'
import { AppSetting } from '../utils/AppSetting'
const AuthStr = 'Bearer '.concat(Claims.getToken());
const axios = require('axios');
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

class BookDataService {
    createBook = (book) => {
        return axios.post(`${AppSetting.BASE_URL}/book/`, book, { headers: { Authorization: AuthStr } });
    };

    getBookbyTitle = (title) => {
        return axios.get(`${AppSetting.BASE_URL}/book/title/${title}`, { headers: { Authorization: AuthStr } });
    };

    getBookbyGeneral = (genre) => {
        // return axios.get(`${BASE_URL}/book/genre/${genre}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/book/genre/${genre}`, { headers: { Authorization: AuthStr } })

    };

    getUserBooks = () => {
        // return axios.get(`${BASE_URL}/book/genre/${genre}`,{ headers: { Authorization: AuthStr } });
        return axios.get(`${AppSetting.BASE_URL}/book/user/${Claims.getUserName()}`, { headers: { Authorization: AuthStr } })

    };

    searchBooks = (searchRequest) => {
        // return axios.get(`${BASE_URL}/book/genre/${genre}`,{ headers: { Authorization: AuthStr } });
        return axios.post(`${AppSetting.BASE_URL}/book/search`, searchRequest, { headers: { Authorization: AuthStr } });
    };
}

export default new BookDataService();