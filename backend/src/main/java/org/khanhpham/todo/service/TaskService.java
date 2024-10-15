package org.khanhpham.todo.service;

import org.khanhpham.todo.entity.Task;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.TaskRequest;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(Long userId, TaskRequest taskRequest);
    TaskDTO updateTask(Long id, TaskRequest taskRequest);
    Task findTaskById(Long id);
    TaskDTO getTaskByUserIdAndTaskId(Long userId, Long id);
    List<TaskDTO> getTasksByUserId(Long userId);
    List<TaskDTO> getTasksByCompletionStatus(Long userId, boolean isCompleted);
    void deleteTask(Long id);
    TaskDTO updateTaskCompletionStatus(Long id, boolean isCompleted);
    TaskDTO updateTaskImportance(Long id, boolean isImportant);
    List<TaskDTO> getTasksByImportance(Long userId, boolean isImportant);
}
