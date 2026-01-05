package htw.webtech.test.business.service;

import htw.webtech.test.persistence.entity.TodoEntity;
import htw.webtech.test.persistence.repository.TodoRepository;
import htw.webtech.test.rest.model.CreateTodoRequest;
import htw.webtech.test.rest.model.TodoDTO;
import htw.webtech.test.rest.model.UpdateTodoRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDTO> getAll() {
        return todoRepository.findAll()
                .stream()
                .map(TodoService::toDto)
                .toList();
    }

    public TodoDTO create(CreateTodoRequest req) {
        var entity = new TodoEntity();
        entity.setTitle(req.title());
        entity.setNotes(req.notes());
        entity.setDueDate(req.dueDate());
        entity.setCategory(req.category());
        entity.setImportant(Boolean.TRUE.equals(req.important()));
        entity.setCompleted(false);

        return toDto(todoRepository.save(entity));
    }

    public TodoDTO update(long id, UpdateTodoRequest req) {
        var entity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + id));

        if (req.title() != null && !req.title().isBlank()) entity.setTitle(req.title());
        if (req.notes() != null) entity.setNotes(req.notes());
        if (req.dueDate() != null) entity.setDueDate(req.dueDate());
        if (req.category() != null) entity.setCategory(req.category());
        if (req.important() != null) entity.setImportant(req.important());
        if (req.completed() != null) entity.setCompleted(req.completed());

        return toDto(todoRepository.save(entity));
    }

    public TodoDTO toggleCompleted(long id) {
        var entity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + id));
        entity.setCompleted(!entity.isCompleted());
        return toDto(todoRepository.save(entity));
    }

    public TodoDTO toggleImportant(long id) {
        var entity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + id));
        entity.setImportant(!entity.isImportant());
        return toDto(todoRepository.save(entity));
    }

    public void delete(long id) {
        todoRepository.deleteById(id);
    }

    private static TodoDTO toDto(TodoEntity e) {
        return new TodoDTO(
                e.getId(),
                e.getTitle(),
                e.getNotes(),
                e.getDueDate(),
                e.getCategory(),
                e.isImportant(),
                e.isCompleted(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
