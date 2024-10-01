package org.khanhpham.todo.payload.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Task name must not be blank")
    private String task;

    private String description;

    @Future(message = "Deadline must be in the future")
    private LocalDateTime deadline;
}
