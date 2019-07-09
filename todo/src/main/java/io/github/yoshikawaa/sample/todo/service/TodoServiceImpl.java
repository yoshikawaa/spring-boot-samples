package io.github.yoshikawaa.sample.todo.service;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    MessageSource messageSource;

    @Autowired
    TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo create(Todo todo) {
        long unfinishedCount = todoRepository.countByFinished(false);
        if (unfinishedCount >= app.getMaxUnFinishedCount()) {
            throw new BusinessException(messageSource.getMessage("messages.409.over-un-finished",
                    new Object[] { app.getMaxUnFinishedCount() }, LocaleContextHolder.getLocale()));
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
            throw new BusinessException(messageSource.getMessage("messages.409.already-finished",
                    new Object[] { todoId }, LocaleContextHolder.getLocale()));
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

    private Todo findById(String todoId) {
        return todoRepository.findById(todoId).orElseThrow(() -> new ResourceNotFoundException(messageSource
                .getMessage("messages.404.todo-not-found", new Object[] { todoId }, LocaleContextHolder.getLocale())));
    }

}
