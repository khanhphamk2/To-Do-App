import axiosInstance from './axiosConfig';

const request = async (method, url, data = null, config = {}) => {
    return axiosInstance({ method, url, data, ...config, });
};

const get = (url, config) => request('get', url, null, config);
const post = (url, data, config) => request('post', url, data, config);
const put = (url, data, config) => request('put', url, data, config);
const patch = (url, data, config) => request('patch', url, data, config);
const del = (url, config) => request('delete', url, null, config);

const httpRequest = {
    get,
    post,
    put,
    patch,
    delete: del,
};

export default httpRequest;
