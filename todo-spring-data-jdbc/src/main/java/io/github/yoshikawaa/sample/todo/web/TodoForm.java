package io.github.yoshikawaa.sample.todo.web;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoForm {
    @NotEmpty(groups = { TodoFinish.class, TodoDelete.class })
    private String todoId;
    @NotEmpty(groups = TodoCreate.class)
    @Size(min = 1, max = 30, groups = TodoCreate.class)
    private String todoTitle;

    public static interface TodoCreate {
    };

    public static interface TodoFinish {
    };

    public static interface TodoDelete {
    }
}
