import axios from 'axios';

const baseUrl = 'http://localhost:8080/api/v1/auth';

export const login = async (data) => {
    try {
        console.log('data:', data);
        const response = await axios.post(`${baseUrl}/login`, data);
        console.log('response:', response);
        return response;
    } catch (error) {
        console.error('Error logging in:', error.response || error.message);
        throw error;
    }
}

export const register = async (data) => {
    try {
        const response = await axios.post(`${baseUrl}/register`, data);
        return response;
    } catch (error) {
        console.error('Error registering:', error);
        throw error;
    }
}

export const logout = async () => {
    try {
        const response = await axios.post(`${baseUrl}/logout`);
        return response;
    } catch (error) {
        console.error('Error logging out:', error);
        throw error;
    }
}

