package org.khanhpham.todo.service;

import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.TaskRequest;
import org.khanhpham.todo.payload.response.PaginationResponse;

public interface TaskService {
    TaskDTO createTask(TaskRequest taskRequest);
    TaskDTO updateTask(Long id, TaskRequest taskRequest);
    TaskDTO getTaskById(Long id);
    PaginationResponse<TaskDTO> getAllTasks(int pageNumber, int pageSize, String sortBy, String sortDir);
    PaginationResponse<TaskDTO> getAllCompletedTasks(int pageNumber, int pageSize, String sortBy, String sortDir);
    PaginationResponse<TaskDTO> getAllInCompleteTasks(int pageNumber, int pageSize, String sortBy, String sortDir);
    void deleteTask(Long id);
    TaskDTO completeTask(Long id);
}
