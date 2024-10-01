package org.khanhpham.todo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic class to represent a paginated response.
 *
 * @param <T> the type of data being returned
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> data; // List of paginated data
    private int page; // Current page number
    private int limit; // Number of items per page
    private long totalResults; // Total number of results available
    private int totalPages; // Total number of pages
    private boolean last; // Indicates if this is the last page
}
