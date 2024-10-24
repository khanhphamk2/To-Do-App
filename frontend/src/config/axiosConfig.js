import axios from 'axios';

const requestConfig = {
    baseURL: 'http://localhost:8080/api/v1',
};

const axiosInstance = axios.create(requestConfig);

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
    (response) => response?.data,
    (error) => {
        const status = error.response?.status;
        switch (status) {
            case 401:
                alert('Phiên đăng nhập hết hạn, vui lòng đăng nhập lại');
                break;
            case 403:
                alert('Bạn không có quyền truy cập vào trang này');
                break;
            case 500:
                console.error('Lỗi server, vui lòng thử lại sau.');
                break;
            default:
                break;
        }
        return Promise.reject(new Error(error.message || 'An error occurred'));
    }
);

export default axiosInstance;