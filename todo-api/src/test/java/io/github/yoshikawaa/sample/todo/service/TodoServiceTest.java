package io.github.yoshikawaa.sample.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.github.yoshikawaa.sample.todo.domain.Todo;
import io.github.yoshikawaa.sample.todo.exception.BusinessException;
import io.github.yoshikawaa.sample.todo.exception.ResourceNotFoundException;
import io.github.yoshikawaa.sample.todo.properties.AppProperties;
import io.github.yoshikawaa.sample.todo.repository.TodoRepository;

@SpringBootTest
class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @MockBean
    TodoRepository todoRepository;

    @Autowired
    AppProperties app;

    @Test
    void testFindAll() {
        // setup
        // setup mocks
        // @formatter:off
        given(todoRepository.findAll())
                .willReturn(Lists.list(
                        new Todo("1", "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay()),
                        new Todo("2", "todo 2", true, LocalDate.of(2019, 1, 2).atStartOfDay()),
                        new Todo("3", "todo 3", false, LocalDate.of(2019, 1, 3).atStartOfDay())));
        // @formatter:on

        // execute
        Collection<Todo> todos = todoService.findAll();

        // assert
        // @formatter:off
        assertThat(todos).extracting(Todo::getTodoId, Todo::getTodoTitle, Todo::isFinished, Todo::getCreatedAt)
                .hasSize(3).containsExactly(
                        tuple("1", "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay()),
                        tuple("2", "todo 2", true, LocalDate.of(2019, 1, 2).atStartOfDay()),
                        tuple("3", "todo 3", false, LocalDate.of(2019, 1, 3).atStartOfDay()));
        // @formatter:on
    }

    @Test
    void testCreate() {
        // setup
        Todo todo = new Todo();
        todo.setTodoTitle("sample todo");
        LocalDateTime now = LocalDateTime.now();
        // setup mocks
        willDoNothing().given(todoRepository).create(any(Todo.class));

        // execute
        Todo created = todoService.create(todo);

        // assert
        // @formatter:off
        assertThat(created).extracting(Todo::getTodoId, Todo::getTodoTitle, Todo::isFinished)
                .containsExactly(null, "sample todo", false);
        // @formatter:on
        assertThat(created.getCreatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    void testCreateMaxUnFinishedCount() {
        // setup
        Todo todo = new Todo();
        todo.setTodoTitle("sample todo");
        // setup mocks
        given(todoRepository.countByFinished(false)).willReturn(6L);

        // execute & assert
        assertThatThrownBy(() -> todoService.create(todo)).isInstanceOf(BusinessException.class)
                .hasMessageContaining(new Long(app.getMaxUnFinishedCount()).toString());
    }

    @Test
    void testFinish() {
        // setup
        String todoId = "1";
        // setup mocks
        given(todoRepository.findById(todoId)).willReturn(
                Optional.of(new Todo(todoId, "sample todo", false, LocalDate.of(2019, 1, 1).atStartOfDay())));

        // execute
        Todo updated = todoService.finish(todoId);

        // assert
        assertThat(updated).extracting(Todo::getTodoId, Todo::getTodoTitle, Todo::isFinished, Todo::getCreatedAt)
                .containsExactly(todoId, "sample todo", true, LocalDate.of(2019, 1, 1).atStartOfDay());
    }

    @Test
    void testFinishNotFound() {
        // setup
        String todoId = "1";
        // setup mocks
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        // execute & assert
        assertThatThrownBy(() -> todoService.finish(todoId)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(todoId);
    }

    @Test
    void testFinishAlreadyFinished() {
        // setup
        String todoId = "1";
        // setup mocks
        given(todoRepository.findById(todoId)).willReturn(
                Optional.of(new Todo(todoId, "sample todo", true, LocalDate.of(2019, 1, 1).atStartOfDay())));

        // execute & assert
        assertThatThrownBy(() -> todoService.finish(todoId)).isInstanceOf(BusinessException.class)
                .hasMessageContaining(todoId);
    }

    @Test
    void testDelete() {
        // setup
        String todoId = "1";
        // setup mocks
        given(todoRepository.findById(todoId)).willReturn(
                Optional.of(new Todo(todoId, "sample todo", false, LocalDate.of(2019, 1, 1).atStartOfDay())));

        // execute
        todoService.delete(todoId);

        // assert
        verify(todoRepository, times(1)).deleteById(argThat(arg -> todoId.equals(arg.getTodoId())));
    }

}
