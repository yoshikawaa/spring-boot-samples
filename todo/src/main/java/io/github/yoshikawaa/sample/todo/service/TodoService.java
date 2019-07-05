package io.github.yoshikawaa.sample.todo.service;

import java.util.Collection;

import io.github.yoshikawaa.sample.todo.domain.Todo;

public interface TodoService {
    Collection<Todo> findAll();
    Todo create(Todo todo);
    Todo finish(String todoId);
    void delete(String todoId);
}
