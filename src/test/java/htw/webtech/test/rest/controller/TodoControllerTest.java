package htw.webtech.test.rest.controller;

import htw.webtech.test.business.service.TodoService;
import htw.webtech.test.rest.model.TodoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TodoController.class)
@Import(RestExceptionHandler.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @Test
    void getAll_returns200() throws Exception {
        when(todoService.getAll()).thenReturn(List.of(dto(1L)));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void create_valid_returns201() throws Exception {
        when(todoService.create(any())).thenReturn(dto(10L));

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"abcd\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void create_invalidTitle_returns400() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"abc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("mindestens 4 Zeichen")));

        verifyNoInteractions(todoService);
    }

    @Test
    void update_returns200() throws Exception {
        when(todoService.update(eq(5L), any())).thenReturn(dto(5L));

        mockMvc.perform(put("/api/todos/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"abcd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void toggleCompleted_returns200() throws Exception {
        when(todoService.toggleCompleted(7L)).thenReturn(dto(7L));

        mockMvc.perform(patch("/api/todos/7/toggle-completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void toggleImportant_returns200() throws Exception {
        when(todoService.toggleImportant(8L)).thenReturn(dto(8L));

        mockMvc.perform(patch("/api/todos/8/toggle-important"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8));
    }

    @Test
    void delete_returns204() throws Exception {
        doNothing().when(todoService).delete(9L);

        mockMvc.perform(delete("/api/todos/9"))
                .andExpect(status().isNoContent());

        verify(todoService).delete(9L);
    }

    private static TodoDTO dto(Long id) {
        return new TodoDTO(
                id,
                "abcd",
                "n",
                LocalDate.of(2026, 1, 20),
                "uni",
                false,
                false,
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z")
        );
    }
}
