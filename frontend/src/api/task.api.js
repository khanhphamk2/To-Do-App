import axios from 'axios';

const baseUrl = 'http://localhost:8080/v1/api/tasks';

export const createTask = async (data) => {
    try {
        const response = await axios.post(`${baseUrl}/`, data);
        return response;
    } catch (error) {
        console.error('Error creating task:', error);
        throw error;
    }
};

export const deleteTask = async (id) => {
    try {
        const response = await axios.delete(`${baseUrl}/${id}`);
        return response.data;
    } catch (error) {
        console.error('Error deleting task:', error);
        throw error;
    }
};

export const getAllTask = async () => {
    try {
        const response = await axios.get(`${baseUrl}/`);
        return response.data;
    } catch (error) {
        console.error('Error getting all tasks:', error);
        throw error;
    }
};

export const getCompletedTask = async () => {
    try {
        const response = await axios.get(`${baseUrl}/completed`);
        return response.data;
    } catch (error) {
        console.error('Error getting completed tasks:', error);
        throw error;
    }
};

export const getIncompletedTask = async () => {
    try {
        const response = await axios.get(`${baseUrl}/uncomplete`);
        return response.data;
    } catch (error) {
        console.error('Error getting incompleted tasks:', error);
        throw error;
    }
};

export const updateTask = async (id, data) => {
    try {
        const response = await axios.get(`${baseUrl}/${id}`, data);
        return response.data;
    } catch (error) {
        console.error('Error updating task:', error);
        throw error;
    }
};
