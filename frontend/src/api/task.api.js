import httpRequest from '../config/httpRequest';

export const createTask = async (data) => {
    try {
        // const response = await httpRequest.post("/tasks/", data);
        return data;
    } catch (error) {
        console.error('Error creating task:', error);
        throw error;
    }
};

export const deleteTask = async (id) => {
    try {
        const response = await httpRequest.delete(`/tasks/${id}`);
        return response.data;
    } catch (error) {
        console.error('Error deleting task:', error);
        throw error;
    }
};

export const getAllTask = async () => {
    try {
        const response = await httpRequest.get("/tasks");
        return response;
    } catch (error) {
        console.error('API error getting all tasks:', error);
        throw error;
    }
};

export const getCompletedTask = async () => {
    try {
        const response = await httpRequest.get(`/tasks/completed`);
        return response.data;
    } catch (error) {
        console.error('Error getting completed tasks:', error);
        throw error;
    }
};

export const getIncompletedTask = async () => {
    try {
        const response = await httpRequest.get(`/tasks/uncomplete`);
        return response.data;
    } catch (error) {
        console.error('Error getting incompleted tasks:', error);
        throw error;
    }
};

export const updateTask = async (id, data) => {
    try {
        const response = await httpRequest.get(`/tasks/${id}`, data);
        return response.data;
    } catch (error) {
        console.error('Error updating task:', error);
        throw error;
    }
};

export const changeTaskImportant = async (id) => {
    try {
        console.log(`Task ID: ${id}, Change task important`);
        // const response = await httpRequest.get(`/tasks/${id}/important`);
        // return response;
    } catch (error) {
        console.error('Error changing task important:', error);
        throw error;
    }
}

export const changeTaskCompleted = async (id) => {
    try {
        console.log(`Task ID: ${id}, Change task completed`);
        // const response = await httpRequest.get(`/tasks/${id}/completed`);
        // return response;
    } catch (error) {
        console.error('Error changing task completed:', error);
        throw error;
    }
}