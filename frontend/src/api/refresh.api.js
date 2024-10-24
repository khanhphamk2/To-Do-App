import axios from 'axios';
import { checkNullish } from '../utils/checkNullish';

export const baseApiUrl = 'http://localhost:8080/v1/api';

export const postGetRefreshToken = () => {
    return axios({
        method: 'post',
        baseURL: baseApiUrl,
        url: '/auth/refresh-tokens',
        data: {
            refreshToken: checkNullish(sessionStorage.get('refreshToken')),
        },
    });
};
