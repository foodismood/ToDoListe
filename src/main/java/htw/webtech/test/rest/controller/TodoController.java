package htw.webtech.test.rest.controller;

import htw.webtech.test.business.service.TodoService;
import htw.webtech.test.rest.model.CreateTodoRequest;
import htw.webtech.test.rest.model.TodoDTO;
import htw.webtech.test.rest.model.UpdateTodoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoDTO>> getAll() {
        return ResponseEntity.ok(todoService.getAll());
    }

    @PostMapping
    public ResponseEntity<TodoDTO> create(@Valid @RequestBody CreateTodoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateTodoRequest request) {
        return ResponseEntity.ok(todoService.update(id, request));
    }

    @PatchMapping("/{id}/toggle-completed")
    public ResponseEntity<TodoDTO> toggleCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.toggleCompleted(id));
    }

    @PatchMapping("/{id}/toggle-important")
    public ResponseEntity<TodoDTO> toggleImportant(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.toggleImportant(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
