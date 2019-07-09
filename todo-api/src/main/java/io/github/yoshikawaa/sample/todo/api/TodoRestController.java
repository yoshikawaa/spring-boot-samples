package io.github.yoshikawaa.sample.todo.api;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.yoshikawaa.sample.todo.domain.Todo;
import io.github.yoshikawaa.sample.todo.service.TodoService;

@RestController
@RequestMapping("/todos")
public class TodoRestController {

    @Autowired
    TodoService todoService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public List<TodoResource> getTodos() {
        // @formatter:off
        return todoService.findAll()
                .stream().map(todo -> modelMapper.map(todo, TodoResource.class))
                .collect(toList());
        // @formatter:on
    }

    @GetMapping("/{todoId}")
    public TodoResource getTodo(@PathVariable("todoId") String todoId) {
        return modelMapper.map(todoService.findById(todoId), TodoResource.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResource postTodos(@RequestBody @Validated TodoResource todoResource) {
        Todo todo = modelMapper.map(todoResource, Todo.class);
        return modelMapper.map(todoService.create(todo), TodoResource.class);
    }

    @PutMapping("/{todoId}")
    public TodoResource putTodo(@PathVariable("todoId") String todoId) {
        return modelMapper.map(todoService.finish(todoId), TodoResource.class);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable("todoId") String todoId) {
        todoService.delete(todoId);
    }

}
