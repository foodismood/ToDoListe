package htw.webtech.test.rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTodoRequest(
        @NotBlank
        @Size(min = 4, max = 200, message = "Der Aufgabenname ist zu kurz (mindestens 4 Zeichen).")
        String title,
        @Size(max = 1000) String notes,
        LocalDate dueDate,
        @Size(max = 60) String category,
        Boolean important
)
{}
