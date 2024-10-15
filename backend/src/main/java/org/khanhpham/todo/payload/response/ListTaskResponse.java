package org.khanhpham.todo.payload.response;

import lombok.*;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.khanhpham.todo.payload.dto.UserDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListTaskResponse {
    private UserDTO user;
    private List<TaskDTO> tasks;
}
