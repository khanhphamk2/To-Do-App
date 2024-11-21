import axios from 'axios';
import { checkNullish } from '../utils/checkNullish';
import { postGetRefreshToken } from '../api/auth.api';

const requestConfig = {
    baseURL: 'http://localhost:8080/api/v1',
};

const axiosInstance = axios.create(requestConfig);

const middlewareRefresh = async (error) => {
    try {
        // Check if the refresh token is available
        if (checkNullish(sessionStorage.get('refreshToken'))) {
            const data = await postGetRefreshToken(); // Call the API to get a new refresh token

            sessionStorage.setItem('accessToken', data.accessToken);
            sessionStorage.setItem('refreshToken', data.refreshToken);

            // Update cookies with the new credentials
            // setCredentialsToCookies({
            //     accessToken: data.access.token,
            //     refreshToken: data.refresh.token,
            // });

            // Update the Authorization header in the original request
            if (error?.config?.headers) {
                error.config.headers.Authorization = `Bearer ${data.access.token}`;
            }

            // Retry the original request
            return axios(error.config);
        }
    } catch (err) {
        // If refresh fails, clear all tokens and redirect to login
        // clearCookiesAndLocalStorage();
        window.location.replace('/login');
        return Promise.reject(new Error(err.message || 'An error occurred'));
    }
};



axiosInstance.interceptors.request.use(
    (config) => {
        if (!config.url.includes('/auth/login')) {
            const accessToken = sessionStorage.getItem('accessToken') || '';
            if (accessToken && config.headers) {
                config.headers.Authorization = `Bearer ${accessToken}`;
            }
        }
        console.log('config', config);
        return config;
    },
    (error) => Promise.reject(new Error(error.message || 'An error occurred'))
);

axiosInstance.interceptors.response.use(
    // (response) => console.log(response?.data),
    (response) => {
        console.log(response?.data);
        return response?.data;
    },
    (error) => {
        console.error('Error response:', error.response || error.message);
        const status = error.response?.status;
        switch (status) {
            case 400:
                if (error.response?.data) {
                    const errorMessages = Object.entries(error.response.data)
                        .map(([field, message]) => `${field}: ${message}`)
                        .join('\n');

                    alert(`Bad request. Please check your input.\nErrors:\n${errorMessages}`);
                } else {
                    alert(`Bad request. Please check your input.\nError: ${error.message || "Unknown error"}`);
                }
                break;
            case 401:
                middlewareRefresh(error);
                break;
            case 403:
                alert('You do not have permission to access this resource.');
                break;
            case 500:
                alert('Internal server error. Please try again later.');
                console.error('Internal server error:', error.response || error.message);
                break;
            default:
                break;
        }
        return Promise.reject(new Error(error.message || 'An error occurred'));
    }
);

export default axiosInstance;