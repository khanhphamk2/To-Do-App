package org.khanhpham.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
