package org.khanhpham.todo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private boolean isCompleted;
    private boolean isImportant;
    private LocalDate date;
    private LocalTime time;
    private Long userId;
}
