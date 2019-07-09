package io.github.yoshikawaa.sample.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import io.github.yoshikawaa.sample.todo.domain.Todo;

@MybatisTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Test
    @Sql(statements = "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', false, '2019-01-01')")
    void testFindById() {
        // setup
        String todoId = todoRepository.findAll().iterator().next().getTodoId();

        // execute
        Todo todo = todoRepository.findById(todoId).get();

        // assert
        assertThat(todo).extracting(Todo::getTodoTitle, Todo::isFinished, Todo::getCreatedAt)
                .containsExactly("sample todo", false, LocalDate.of(2019, 1, 1).atStartOfDay());
    }

    @Test
    @Sql(statements = "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', false, '2019-01-01')")
    void testFindAll() {
        // execute
        Collection<Todo> todos = todoRepository.findAll();

        // assert
        assertThat(todos).hasSize(1).extracting(Todo::getTodoTitle, Todo::isFinished, Todo::getCreatedAt)
                .containsExactly(tuple("sample todo", false, LocalDate.of(2019, 1, 1).atStartOfDay()));
    }

    @Test
    void testCreate() {
        // setup
        Todo todo = new Todo();
        todo.setTodoTitle("sample todo");
        todo.setFinished(false);
        todo.setCreatedAt(LocalDate.of(2019, 1, 1).atStartOfDay());

        // execute
        todoRepository.create(todo);

        // assert
        Todo created = todoRepository.findAll().iterator().next();
        assertThat(created).extracting(Todo::getTodoTitle, Todo::isFinished, Todo::getCreatedAt)
                .containsExactly("sample todo", false, LocalDate.of(2019, 1, 1).atStartOfDay());
    }

    @Test
    @Sql(statements = "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', false, '2019-01-01')")
    void testUpdateById() {
        // setup
        Todo todo = todoRepository.findAll().iterator().next();
        todo.setTodoTitle("updated todo");
        todo.setFinished(true);
        todo.setCreatedAt(LocalDate.of(2019, 1, 2).atStartOfDay());

        // execute
        boolean result = todoRepository.updateById(todo);

        // assert
        assertThat(result).isTrue();
        Todo updated = todoRepository.findById(todo.getTodoId()).get();
        assertThat(updated).extracting(Todo::getTodoTitle, Todo::isFinished, Todo::getCreatedAt)
                .containsExactly("updated todo", true, LocalDate.of(2019, 1, 2).atStartOfDay());
    }

    @Test
    void testUpdateByIdNotFound() {
        // setup
        Todo todo = new Todo();
        todo.setTodoId("1");
        todo.setTodoTitle("updated todo");
        todo.setFinished(true);
        todo.setCreatedAt(LocalDate.of(2019, 1, 2).atStartOfDay());

        // execute
        boolean result = todoRepository.updateById(todo);

        // assert
        assertThat(result).isFalse();
    }

    @Test
    @Sql(statements = "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', false, '2019-01-01')")
    void testDeleteById() {
        // setup
        Todo todo = todoRepository.findAll().iterator().next();

        // execute
        todoRepository.deleteById(todo);

        // assert
        assertThat(todoRepository.findById(todo.getTodoId()).isPresent()).isFalse();
    }

    @Test
    @Sql(statements = {
            "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', false, '2019-01-01')",
            "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', true, '2019-01-01')",
            "INSERT INTO todo (todo_title, finished, created_at) VALUES ('sample todo', false, '2019-01-01')" })
    void testCountByFinished() {
        // execute & assert
        assertThat(todoRepository.countByFinished(true)).isEqualTo(1);
        assertThat(todoRepository.countByFinished(false)).isEqualTo(2);
    }

}
