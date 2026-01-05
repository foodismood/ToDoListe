package htw.webtech.test.rest.model;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTodoRequest(
        @Size(max = 200) String title,
        @Size(max = 1000) String notes,
        LocalDate dueDate,
        @Size(max = 60) String category,
        Boolean important,
        Boolean completed
)
{}
