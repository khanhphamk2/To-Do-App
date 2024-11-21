import httpRequest from '../config/httpRequest';

export const createTask = async (data) => {
    try {
        console.log('Create task:', data);
        const response = await httpRequest.post("/tasks", data);
        console.log('Task created:', response);
        return response;
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

export const getImportantTask = async () => {
    try {
        const response = await httpRequest.get(`/tasks/important`);
        return response.data;
    } catch (error) {
        console.error('Error getting important tasks:', error);
        throw error;
    }
}

export const updateTask = async (id, data) => {
    try {
        const response = await httpRequest.put(`/tasks/${id}`, data);
        return response.data;
    } catch (error) {
        console.error('Error updating task:', error);
        throw error;
    }
};

export const changeTaskImportant = async (id, data) => {
    try {
        console.log(`Task ID: ${id}, Change task important`);
        const response = await httpRequest.put(`/tasks/${id}/status?field=important`, data);
        return response;
    } catch (error) {
        console.error('Error changing task important:', error);
        throw error;
    }
}

export const changeTaskCompleted = async (id, data) => {
    try {
        console.log(`Task ID: ${id}, Change task complete`);
        const response = await httpRequest.put(`/tasks/${id}/status?field=completed`, data);
        return response;
    } catch (error) {
        console.error('Error changing task completed:', error);
        throw error;
    }
}