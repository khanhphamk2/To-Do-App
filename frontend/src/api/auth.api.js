import httpRequest from '../config/httpRequest';
import { checkNullish } from '../utils/checkNullish';

export const login = async (data) => {
    try {
        const response = await httpRequest.post('/auth/login', data);
        return response;
    } catch (error) {
        console.error('Error logging in:', error.response || error.message);
        throw error;
    }
}

export const register = async (data) => {
    try {
        const response = await httpRequest.post(`/auth/register`, data);
        return response;
    } catch (error) {
        console.error('Error registering:', error);
        throw error;
    }
}

export const postGetRefreshToken = () => {
    try {
        const response = httpRequest.post('/auth/refresh-tokens', {
            refreshToken: checkNullish(sessionStorage.getItem('refreshToken')),
        });
        return response;
    } catch (error) {
        console.error('Error getting refresh token:', error);
        throw error;
    }
};