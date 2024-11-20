package org.khanhpham.todo.controller;

import jakarta.validation.Valid;
import org.khanhpham.todo.entity.CustomUserDetails;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.ChangeTaskStatusRequest;
import org.khanhpham.todo.payload.request.TaskRequest;
import org.khanhpham.todo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${spring.data.rest.base-path}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> getTasksByFilter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestParam(value = "filterField") String filterField,
                                                          @RequestParam(value = "filterValue") boolean filterValue) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.getTasksByFilter(userId, filterField, filterValue));
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> createTask(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @Valid @RequestBody TaskRequest taskRequest) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.createTask(userId, taskRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskByUserIdAndTaskId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(value = "id") Long id) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.getTaskByUserIdAndTaskId(userId, id));
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

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDTO>  updateTaskStatus(
            @PathVariable(value = "id") Long id,
            @RequestParam("field") String field,
            @RequestBody ChangeTaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, field, request));
    }
}
