package io.github.yoshikawaa.sample.todo.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.github.yoshikawaa.sample.todo.domain.Todo;
import io.github.yoshikawaa.sample.todo.service.TodoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TodoRestControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    TodoService todoService;

    @Test
    void testGetTodos() throws Exception {
        // setup
        Collection<Todo> todos = Lists.list(new Todo("1", "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay()),
                new Todo("2", "todo 2", true, LocalDate.of(2019, 1, 2).atStartOfDay()),
                new Todo("3", "todo 3", false, LocalDate.of(2019, 1, 3).atStartOfDay()));
        // setup mocks
        given(todoService.findAll()).willReturn(todos);

        // execute
        ResponseEntity<Collection<TodoResource>> result = restTemplate.exchange("/todos", HttpMethod.GET, null,
                new ParameterizedTypeReference<Collection<TodoResource>>() {
                });

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(result.getBody())
                .extracting(TodoResource::getTodoId, TodoResource::getTodoTitle, TodoResource::isFinished,
                        TodoResource::getCreatedAt)
                .hasSize(3).containsExactly(tuple("1", "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay()),
                        tuple("2", "todo 2", true, LocalDate.of(2019, 1, 2).atStartOfDay()),
                        tuple("3", "todo 3", false, LocalDate.of(2019, 1, 3).atStartOfDay()));
    }

    @Test
    void testGetTodo() throws Exception {
        // setup
        String todoId = "1";
        Todo todo = new Todo(todoId, "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay());
        // setup mocks
        given(todoService.findById(todoId)).willReturn(todo);

        // execute
        ResponseEntity<TodoResource> result = restTemplate.exchange("/todos/" + todoId, HttpMethod.GET, null,
                TodoResource.class);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(result.getBody())
                .extracting(TodoResource::getTodoId, TodoResource::getTodoTitle, TodoResource::isFinished,
                        TodoResource::getCreatedAt)
                .containsExactly("1", "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay());
    }

    @Test
    void testPostTodos() throws Exception {
        // setup
        String todoTitle = "sample todo";
        TodoResource resource = new TodoResource();
        resource.setTodoTitle(todoTitle);
        Todo todo = new Todo("1", todoTitle, false, LocalDate.of(2019, 1, 1).atStartOfDay());
        // setup mocks
        given(todoService.create(any(Todo.class))).willReturn(todo);

        // execute
        ResponseEntity<TodoResource> result = restTemplate.exchange("/todos", HttpMethod.POST,
                new HttpEntity<TodoResource>(resource), TodoResource.class);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(result.getBody())
                .extracting(TodoResource::getTodoId, TodoResource::getTodoTitle, TodoResource::isFinished,
                        TodoResource::getCreatedAt)
                .containsExactly("1", "sample todo", false, LocalDate.of(2019, 1, 1).atStartOfDay());
    }

    @Test
    void testPostTodosValidationError() throws Exception {
        // setup
        String todoTitle = "";
        TodoResource resource = new TodoResource();
        resource.setTodoTitle(todoTitle);

        // execute
        ResponseEntity<String> result = restTemplate.exchange("/todos", HttpMethod.POST,
                new HttpEntity<TodoResource>(resource), String.class);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(result.getBody()).contains("NotEmpty.todoResource.todoTitle");
    }

    @Test
    void testPutTodo() throws Exception {
        // setup
        String todoId = "1";
        Todo todo = new Todo(todoId, "sample todo", true, LocalDate.of(2019, 1, 1).atStartOfDay());
        // setup mocks
        given(todoService.finish(anyString())).willReturn(todo);

        // execute
        ResponseEntity<TodoResource> result = restTemplate.exchange("/todos/" + todoId, HttpMethod.PUT, null,
                TodoResource.class);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(result.getBody())
                .extracting(TodoResource::getTodoId, TodoResource::getTodoTitle, TodoResource::isFinished,
                        TodoResource::getCreatedAt)
                .containsExactly("1", "sample todo", true, LocalDate.of(2019, 1, 1).atStartOfDay());
    }

    @Test
    void testDeleteTodo() throws Exception {
        // setup
        String todoId = "1";

        // execute
        ResponseEntity<TodoResource> result = restTemplate.exchange("/todos/" + todoId, HttpMethod.DELETE, null,
                TodoResource.class);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.hasBody()).isFalse();
    }

}
