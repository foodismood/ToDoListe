package htw.webtech.test.persistence.repository;

import htw.webtech.test.persistence.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}
