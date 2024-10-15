package org.khanhpham.todo.service.implement;

import org.khanhpham.todo.entity.Task;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.TaskRequest;
import org.khanhpham.todo.repository.TaskRepository;
import org.khanhpham.todo.repository.UserRepository;
import org.khanhpham.todo.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the TaskService interface that handles task-related operations.
 * This service provides methods to create, update, delete, and retrieve tasks,
 * as well as managing task importance and completion status.
 */
@Service
public class TaskServiceImpl implements TaskService {

    public static final String TASK_NOT_FOUND_MESSAGE = "Task with id {0} not found";

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Helper method to find a task by its ID.
     * Throws a RuntimeException if the task is not found.
     *
     * @param id the ID of the task to find
     * @return the found Task entity
     */
    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format(TASK_NOT_FOUND_MESSAGE, id)));
    }

    /**
     * Converts a Task entity to a TaskDTO.
     *
     * @param task the Task entity to convert
     * @return the corresponding TaskDTO
     */
    private TaskDTO convertToDto(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setUserId(task.getUser().getId());
        return taskDTO;
    }

    /**
     * Converts a TaskRequest to a Task entity.
     *
     * @param taskRequest the TaskRequest to convert
     * @return the corresponding Task entity
     */
    private Task convertToEntity(TaskRequest taskRequest) {
        return modelMapper.map(taskRequest, Task.class);
    }

    /**
     * Creates a new task based on the provided TaskRequest.
     * Sets the creation and update timestamps before saving the task.
     *
     * @param taskRequest the request containing task details
     * @return the created TaskDTO
     */
    @Override
    public TaskDTO createTask(Long userId, TaskRequest taskRequest) {
        Task task = convertToEntity(taskRequest);
        task.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException(MessageFormat.format("User with id {0} not found", userId))));
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedDate(now);
        task.setUpdatedDate(now);
        return convertToDto(taskRepository.save(task));
    }

    /**
     * Updates an existing task identified by its ID with the provided TaskRequest details.
     *
     * @param id          the ID of the task to update
     * @param taskRequest the request containing updated task details
     * @return the updated TaskDTO
     */
    @Override
    public TaskDTO updateTask(Long id, TaskRequest taskRequest) {
        Task task = findTaskById(id);
        task.setDate(taskRequest.getDate());
        task.setTime(taskRequest.getTime());
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUpdatedDate(LocalDateTime.now());
        return convertToDto(taskRepository.save(task));
    }

    /**
     * Retrieves all tasks for a specific user.
     *
     * @param userId the ID of the user whose tasks are to be retrieved
     * @return a list of TaskDTOs for the user
     */
    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {
        return taskRepository.findTaskByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Retrieves a task by its ID and the associated user's ID.
     *
     * @param userId the ID of the user who owns the task
     * @param id     the ID of the task to retrieve
     * @return the corresponding TaskDTO
     */
    @Override
    public TaskDTO getTaskByUserIdAndTaskId(Long userId, Long id) {
        return convertToDto(taskRepository.findByUserIdAndId(userId, id));
    }

    /**
     * Retrieves all tasks for a specific user based on their completion status.
     *
     * @param userId      the ID of the user
     * @param isCompleted true to retrieve completed tasks, false for incomplete tasks
     * @return a list of TaskDTOs for the user filtered by completion status
     */
    @Override
    public List<TaskDTO> getTasksByCompletionStatus(Long userId, boolean isCompleted) {
        return taskRepository.findByUserIdAndIsCompleted(userId, isCompleted)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Deletes a task identified by its ID.
     * Throws a RuntimeException if the task is not found.
     *
     * @param id the ID of the task to delete
     */
    @Override
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
    }

    /**
     * Updates the completion status of a task.
     *
     * @param id          the ID of the task to update
     * @param isCompleted true if the task is completed, false if incomplete
     * @return the updated TaskDTO
     */
    @Override
    public TaskDTO updateTaskCompletionStatus(Long id, boolean isCompleted) {
        Task task = findTaskById(id);
        task.setCompleted(isCompleted);
        return convertToDto(taskRepository.save(task));
    }

    /**
     * Updates the importance status of a task.
     *
     * @param id         the ID of the task to update
     * @param isImportant true if the task is important, false if unimportant
     * @return the updated TaskDTO
     */
    @Override
    public TaskDTO updateTaskImportance(Long id, boolean isImportant) {
        Task task = findTaskById(id);
        task.setImportant(isImportant);
        return convertToDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDTO> getTasksByImportance(Long userId, boolean isImportant) {
        return taskRepository.findByUserIdAndIsImportant(userId, isImportant)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
}
