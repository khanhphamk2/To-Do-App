package org.khanhpham.todo.controller;

import jakarta.validation.Valid;
import org.khanhpham.todo.entity.CustomUserDetails;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.request.ChangeTaskStatusRequest;
import org.khanhpham.todo.payload.request.TaskRequest;
import org.khanhpham.todo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Get all tasks", description = "Retrieve all tasks assigned to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @AuthenticationPrincipal @Parameter(description = "The authenticated user's details") CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @Operation(summary = "Get tasks by filter", description = "Retrieve tasks based on the provided filter criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> getTasksByFilter(
            @AuthenticationPrincipal @Parameter(description = "The authenticated user's details") CustomUserDetails userDetails,
            @RequestParam(value = "filterField") @Parameter(description = "The field to filter by") String filterField,
            @RequestParam(value = "filterValue") @Parameter(description = "The value to filter by") boolean filterValue) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.getTasksByFilter(userId, filterField, filterValue));
    }

    @Operation(summary = "Create a new task", description = "Create a new task for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created task",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid task input")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @AuthenticationPrincipal @Parameter(description = "The authenticated user's details") CustomUserDetails userDetails,
            @Valid @RequestBody @Parameter(description = "The task data to be created") TaskRequest taskRequest) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.createTask(userId, taskRequest));
    }

    @Operation(summary = "Get task by userId and taskId", description = "Retrieve a task by its ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskByUserIdAndTaskId(
            @AuthenticationPrincipal @Parameter(description = "The authenticated user's details") CustomUserDetails userDetails,
            @PathVariable(value = "id") @Parameter(description = "The task ID") Long id) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(taskService.getTaskByUserIdAndTaskId(userId, id));
    }

    @Operation(summary = "Update task", description = "Update an existing task for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated task",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid task input"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable(value = "id") @Parameter(description = "The task ID") Long id,
            @Valid @RequestBody @Parameter(description = "The updated task data") TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest));
    }

    @Operation(summary = "Delete task", description = "Delete an existing task for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable(value = "id") @Parameter(description = "The task ID") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update task status", description = "Update the status of a specific task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated task status",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable(value = "id") @Parameter(description = "The task ID") Long id,
            @RequestParam("field") @Parameter(description = "The field to update the status") String field,
            @RequestBody @Parameter(description = "The status change request") ChangeTaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, field, request));
    }
}
