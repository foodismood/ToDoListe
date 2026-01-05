package htw.webtech.test.rest.model;

import java.time.Instant;
import java.time.LocalDate;

public record TodoDTO(
        Long id,
        String title,
        String notes,
        LocalDate dueDate,
        String category,
        boolean important,
        boolean completed,
        Instant createdAt,
        Instant updatedAt
) {}
