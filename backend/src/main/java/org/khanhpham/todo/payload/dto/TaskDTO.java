package org.khanhpham.todo.payload.dto;

import lombok.*;
import org.khanhpham.todo.entity.Task;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link Task}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDTO extends AudiDTO{
    Long id;
    String title;
    String description;
    LocalDate date;
    LocalTime time;
    boolean isCompleted;
    boolean isImportant;
    Long userId;
}