package io.github.yoshikawaa.sample.todo.repository;

import org.springframework.data.repository.CrudRepository;

import io.github.yoshikawaa.sample.todo.domain.Todo;

public interface TodoRepository extends CrudRepository<Todo, String> {
}
