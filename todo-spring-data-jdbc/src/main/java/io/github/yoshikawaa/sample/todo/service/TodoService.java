package io.github.yoshikawaa.sample.todo.service;

import io.github.yoshikawaa.sample.todo.domain.Todo;

public interface TodoService {
    Iterable<Todo> findAll();
    Todo create(Todo todo);
    Todo finish(String todoId);
    void delete(String todoId);
}
