package io.github.yoshikawaa.sample.todo.service;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.yoshikawaa.sample.todo.domain.Todo;
import io.github.yoshikawaa.sample.todo.exception.BusinessException;
import io.github.yoshikawaa.sample.todo.exception.ResourceNotFoundException;
import io.github.yoshikawaa.sample.todo.properties.AppProperties;
import io.github.yoshikawaa.sample.todo.repository.TodoRepository;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    AppProperties app;

    @Autowired
    TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Todo findById(String todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new ResourceNotFoundException("The requested Todo is not found. (id=" + todoId + ")"));
    }

    @Override
    public Todo create(Todo todo) {
        long unfinishedCount = todoRepository.countByFinished(false);
        if (unfinishedCount >= app.getMaxUnFinishedCount()) {
            throw new BusinessException(
                    "The count of un-finished Todo must not be over " + app.getMaxUnFinishedCount());
        }

        todo.setFinished(false);
        todo.setCreatedAt(LocalDateTime.now());
        todoRepository.create(todo);
        return todo;
    }

    @Override
    public Todo finish(String todoId) {
        Todo todo = findById(todoId);
        if (todo.isFinished()) {
            throw new BusinessException("The requested Todo is already finished. (id=" + todoId + ")");
        }

        todo.setFinished(true);
        todoRepository.updateById(todo);
        return todo;
    }

    @Override
    public void delete(String todoId) {
        Todo todo = findById(todoId);
        todoRepository.deleteById(todo);
    }

}
