package org.khanhpham.todo.payload.dto;

import lombok.Data;

@Data
public abstract class AudiDTO {
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
}