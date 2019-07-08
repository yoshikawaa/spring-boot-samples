package io.github.yoshikawaa.sample.todo.web;

import static org.hamcrest.collection.IsIn.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.yoshikawaa.sample.todo.domain.Todo;
import io.github.yoshikawaa.sample.todo.exception.ResourceNotFoundException;
import io.github.yoshikawaa.sample.todo.service.TodoService;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(TodoController.class)
//@ImportAutoConfiguration(ModelMapperAutoConfiguration.class)
class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TodoService todoService;

    @Test
    void testList() throws Exception {
        // setup
        Collection<Todo> todos = Lists.list(new Todo("1", "todo 1", false, LocalDate.of(2019, 1, 1).atStartOfDay()),
                new Todo("2", "todo 2", true, LocalDate.of(2019, 1, 2).atStartOfDay()),
                new Todo("3", "todo 3", false, LocalDate.of(2019, 1, 3).atStartOfDay()));
        // setup mocks
        given(todoService.findAll()).willReturn(todos);

        // execute & assert
        // @formatter:off
        mvc.perform(get("/todo/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("list"))
            .andExpect(model().attribute("todos", todos));
        // @formatter:on
    }

    @Test
    void testCreate() throws Exception {
        // setup
        String todoTitle = "sample todo";

        // execute & assert
        // @formatter:off
        mvc.perform(post("/todo/create").param("todoTitle", todoTitle))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/list"))
            .andExpect(model().hasNoErrors())
            .andExpect(flash().attribute("successMessage", "Created successfully!"));
        // @formatter:on

        // assert
        verify(todoService, times(1)).create(argThat(arg -> todoTitle.equals(arg.getTodoTitle())));
    }

    @Test
    void testCreateValidationError() throws Exception {
        {
            // setup
            String todoTitle = "";

            // execute & assert
            // @formatter:off
            mvc.perform(post("/todo/create").param("todoTitle", todoTitle))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoTitle", in(Lists.list("NotEmpty", "Size"))));
            // @formatter:on

            // assert
            verify(todoService, times(0)).create(any(Todo.class));
        }

        {
            // setup
            String todoTitle = "123456789a123456789b123456789c1";

            // execute & assert
            // @formatter:off
            mvc.perform(post("/todo/create").param("todoTitle", todoTitle))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoTitle", "Size"));
            // @formatter:on

            // assert
            verify(todoService, times(0)).create(any(Todo.class));
        }
    }

    @Test
    void testFinish() throws Exception {
        // setup
        String todoId = "1";

        // execute & assert
        // @formatter:off
        mvc.perform(post("/todo/finish").param("todoId", todoId))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/list"))
            .andExpect(model().hasNoErrors())
            .andExpect(flash().attribute("successMessage", "Finished successfully!"));
        // @formatter:on

        // assert
        verify(todoService, times(1)).finish(argThat(arg -> todoId.equals(arg)));
    }

    @Test
    void testFinishValidationError() throws Exception {
        // setup
        String todoId = "";

        // execute & assert
        // @formatter:off
        mvc.perform(post("/todo/finish").param("todoId", todoId))
            .andExpect(status().isOk())
            .andExpect(view().name("list"))
            .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoId", "NotEmpty"));
        // @formatter:on

        // assert
        verify(todoService, times(0)).finish(anyString());
    }

    @Test
    void testFinishResourceNotFound() throws Exception {
        // setup
        String todoId = "1";
        // setup mocks
        given(todoService.finish(anyString())).willThrow(new ResourceNotFoundException("sample exception"));

        // execute & assert
        // @formatter:off
        mvc.perform(post("/todo/finish").param("todoId", todoId))
            .andExpect(status().isNotFound());
        // @formatter:on

        // assert
        verify(todoService, times(1)).finish(anyString());
    }

    @Test
    void testDelete() throws Exception {
        // setup
        String todoId = "1";

        // execute & assert
        // @formatter:off
        mvc.perform(post("/todo/delete").param("todoId", todoId))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/list"))
            .andExpect(model().hasNoErrors())
            .andExpect(flash().attribute("successMessage", "Deleted successfully!"));
        // @formatter:on

        // assert
        verify(todoService, times(1)).delete(argThat(arg -> todoId.equals(arg)));
    }

    @Test
    void testDeleteValidationError() throws Exception {
        // setup
        String todoId = "";

        // execute & assert
        // @formatter:off
        mvc.perform(post("/todo/delete").param("todoId", todoId))
            .andExpect(status().isOk())
            .andExpect(view().name("list"))
            .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoId", "NotEmpty"));
        // @formatter:on

        // assert
        verify(todoService, times(0)).delete(anyString());
    }

}
