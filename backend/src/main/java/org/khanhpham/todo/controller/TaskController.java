package org.khanhpham.todo.controller;

import jakarta.validation.Valid;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.TaskRequest;
import org.khanhpham.todo.payload.response.PaginationResponse;
import org.khanhpham.todo.service.TaskService;
import org.khanhpham.todo.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base-path}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<TaskDTO>> getAllTasks(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "limit", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir
    ) {
        return ResponseEntity.ok(taskService.getAllTasks(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/completed")
    public ResponseEntity<PaginationResponse<TaskDTO>> getCompletedTasks(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "limit", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir
    ) {
        return ResponseEntity.ok(taskService.getAllCompletedTasks(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/incompleted")
    public ResponseEntity<PaginationResponse<TaskDTO>> getIncompleteTasks(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "limit", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir
    ) {
        return ResponseEntity.ok(taskService.getAllInCompleteTasks(pageNumber, pageSize, sortBy, sortDir));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable(value = "id") Long id,
                                              @Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable(value = "id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> completeTask(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(taskService.completeTask(id));
    }
}
