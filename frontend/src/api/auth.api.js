import httpRequest from '../config/httpRequest';

export const login = async (data) => {
    try {
        const response = await httpRequest.post('/auth/login', data);
        console.log('response', response);
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
