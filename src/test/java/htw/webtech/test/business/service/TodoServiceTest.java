package htw.webtech.test.business.service;

import htw.webtech.test.persistence.entity.TodoEntity;
import htw.webtech.test.persistence.repository.TodoRepository;
import htw.webtech.test.rest.model.CreateTodoRequest;
import htw.webtech.test.rest.model.UpdateTodoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoService todoService;

    @Test
    void getAll_returnsDtos() {
        var e1 = entity(1L, "Task 1", false, false);
        var e2 = entity(2L, "Task 2", true, true);
        when(todoRepository.findAll()).thenReturn(List.of(e1, e2));

        var result = todoService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).title()).isEqualTo("Task 1");
        assertThat(result.get(1).important()).isTrue();
        assertThat(result.get(1).completed()).isTrue();
        verify(todoRepository).findAll();
    }

    @Test
    void create_savesEntityAndReturnsDto() {
        var req = new CreateTodoRequest(
                "New Task",
                "notes",
                LocalDate.of(2026, 1, 20),
                "uni",
                true
        );

        when(todoRepository.save(any(TodoEntity.class))).thenAnswer(inv -> {
            var e = inv.getArgument(0, TodoEntity.class);
            e.setId(10L);
            return e;
        });

        var dto = todoService.create(req);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.title()).isEqualTo("New Task");
        assertThat(dto.important()).isTrue();
        assertThat(dto.completed()).isFalse();

        var captor = ArgumentCaptor.forClass(TodoEntity.class);
        verify(todoRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("New Task");
        assertThat(captor.getValue().isCompleted()).isFalse();
    }

    @Test
    void update_updatesFields() {
        var existing = entity(5L, "Old", false, false);
        when(todoRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any(TodoEntity.class))).thenAnswer(inv -> inv.getArgument(0, TodoEntity.class));

        var req = new UpdateTodoRequest(
                "Updated",
                "new notes",
                LocalDate.of(2026, 2, 1),
                "work",
                true,
                true
        );

        var dto = todoService.update(5L, req);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.title()).isEqualTo("Updated");
        assertThat(dto.notes()).isEqualTo("new notes");
        assertThat(dto.category()).isEqualTo("work");
        assertThat(dto.important()).isTrue();
        assertThat(dto.completed()).isTrue();
    }

    @Test
    void update_whenNotFound_throws() {
        when(todoRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.update(404L, new UpdateTodoRequest(null, null, null, null, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo not found");
    }

    @Test
    void toggleCompleted_togglesValue() {
        var existing = entity(7L, "X", false, false);
        when(todoRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any(TodoEntity.class))).thenAnswer(inv -> inv.getArgument(0, TodoEntity.class));

        var dto1 = todoService.toggleCompleted(7L);
        var dto2 = todoService.toggleCompleted(7L);

        assertThat(dto1.completed()).isTrue();
        assertThat(dto2.completed()).isFalse();
    }

    @Test
    void toggleImportant_togglesValue() {
        var existing = entity(8L, "X", false, false);
        when(todoRepository.findById(8L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any(TodoEntity.class))).thenAnswer(inv -> inv.getArgument(0, TodoEntity.class));

        var dto1 = todoService.toggleImportant(8L);
        var dto2 = todoService.toggleImportant(8L);

        assertThat(dto1.important()).isTrue();
        assertThat(dto2.important()).isFalse();
    }

    @Test
    void delete_delegatesToRepository() {
        todoService.delete(9L);
        verify(todoRepository).deleteById(9L);
    }

    private static TodoEntity entity(Long id, String title, boolean important, boolean completed) {
        var e = new TodoEntity();
        e.setId(id);
        e.setTitle(title);
        e.setNotes("n");
        e.setDueDate(LocalDate.of(2026, 1, 1));
        e.setCategory("c");
        e.setImportant(important);
        e.setCompleted(completed);
        return e;
    }
}
