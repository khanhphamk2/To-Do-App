package org.khanhpham.todo.service.implement;

import org.khanhpham.todo.entity.Task;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.TaskRequest;
import org.khanhpham.todo.payload.response.PaginationResponse;
import org.khanhpham.todo.repository.TaskRepository;
import org.khanhpham.todo.service.TaskService;
import org.khanhpham.todo.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Implementation of the TaskService interface that handles task-related operations.
 * This service provides methods to create, update, delete, and retrieve tasks,
 * as well as pagination support for listing tasks.
 */
@Service
public class ITaskService implements TaskService {
    public static final String TASK_NOT_FOUND_MESSAGE = "Task with id {0} not found";

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public ITaskService(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Converts a Task entity to a TaskDTO.
     *
     * @param task the Task entity to convert
     * @return the corresponding TaskDTO
     */
    private TaskDTO convertToDto(Task task) {
        return modelMapper.map(task, TaskDTO.class);
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
     *
     * @param taskRequest the request containing task details
     * @return the created TaskDTO
     */
    @Override
    public TaskDTO createTask(TaskRequest taskRequest) {
        Task task = convertToEntity(taskRequest);
        Task newTask = taskRepository.save(task);
        return convertToDto(newTask);
    }

    /**
     * Updates an existing task identified by its ID with the provided TaskRequest.
     *
     * @param id          the ID of the task to update
     * @param taskRequest the request containing updated task details
     * @return the updated TaskDTO
     */
    @Override
    public TaskDTO updateTask(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException(MessageFormat.format(TASK_NOT_FOUND_MESSAGE, id)));
        task.setDeadline(taskRequest.getDeadline());
        task.setTask(taskRequest.getTask());
        task.setDescription(taskRequest.getDescription());
        Task updateTask = taskRepository.save(task);
        return convertToDto(updateTask);
    }

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize   the number of tasks per page
     * @param sortBy     the attribute to sort by
     * @param sortDir    the direction of sorting (ascending/descending)
     * @return a PaginationResponse containing the list of TaskDTOs
     */
    @Override
    public PaginationResponse<TaskDTO> getAllTasks(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtils.convertToPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<Task> listOfTask = tasks.getContent();
        List<TaskDTO> content = listOfTask.stream().map(this::convertToDto).toList();
        return PaginationUtils.createPaginationResponse(content, tasks);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve
     * @return the corresponding TaskDTO
     */
    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException(MessageFormat.format(TASK_NOT_FOUND_MESSAGE, id)));
        return convertToDto(task);
    }

    /**
     * Retrieves all completed tasks with pagination support.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize   the number of tasks per page
     * @param sortBy     the attribute to sort by
     * @param sortDir    the direction of sorting (ascending/descending)
     * @return a PaginationResponse containing the list of TaskDTOs
     */
    @Override
    public PaginationResponse<TaskDTO> getAllCompletedTasks(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtils.convertToPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Task> tasks = taskRepository.findByCompletedTrue(pageable);
        List<Task> listOfTask = tasks.getContent();
        List<TaskDTO> content = listOfTask.stream().map(this::convertToDto).toList();
        return PaginationUtils.createPaginationResponse(content, tasks);
    }

    /**
     * Retrieves all incomplete tasks with pagination support.
     *
             * @param pageNumber the page number to retrieve
     * @param pageSize   the number of tasks per page
     * @param sortBy     the attribute to sort by
     * @param sortDir    the direction of sorting (ascending/descending)
     * @return a PaginationResponse containing the list of TaskDTOs
     */
    @Override
    public PaginationResponse<TaskDTO> getAllInCompleteTasks(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtils.convertToPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Task> tasks = taskRepository.findByCompletedFalse(pageable);
        List<Task> listOfTask = tasks.getContent();
        List<TaskDTO> content = listOfTask.stream().map(this::convertToDto).toList();
        return PaginationUtils.createPaginationResponse(content, tasks);
    }

    /**
     * Deletes a task identified by its ID.
     *
     * @param id the ID of the task to delete
     */
    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException(MessageFormat.format(TASK_NOT_FOUND_MESSAGE, id)));
        taskRepository.delete(task);
    }

    /**
     * Marks a task as completed identified by its ID.
     *
     * @param id the ID of the task to mark as completed
     * @return the updated TaskDTO
     */
    @Override
    public TaskDTO completeTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException(MessageFormat.format(TASK_NOT_FOUND_MESSAGE, id)));
        task.setCompleted(true);
        Task completeTask = taskRepository.save(task);
        return convertToDto(completeTask);
    }
}
