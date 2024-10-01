package org.khanhpham.todo.payload.dto;

import lombok.*;
import org.khanhpham.todo.entity.Task;

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
    String task;
    String description;
    boolean completed;
}