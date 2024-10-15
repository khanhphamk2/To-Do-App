package org.khanhpham.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;
}
