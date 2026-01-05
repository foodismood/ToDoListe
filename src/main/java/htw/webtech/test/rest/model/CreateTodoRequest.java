package htw.webtech.test.rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTodoRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 1000) String notes,
        LocalDate dueDate,
        @Size(max = 60) String category,
        Boolean important
)
{}
