package io.github.yoshikawaa.sample.todo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.github.yoshikawaa.sample.todo.domain.Todo;

public interface TodoRepository extends CrudRepository<Todo, String> {

    @Query("SELECT COUNT(*) FROM todo WHERE finished = :finished")
   long countByFinished(@Param("finished") boolean finished);
}
