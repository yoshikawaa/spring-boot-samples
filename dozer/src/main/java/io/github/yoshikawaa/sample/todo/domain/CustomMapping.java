package io.github.yoshikawaa.sample.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMapping {
    private String todoId;
    private String todoTitle;
    private boolean finished;
    private String createdAt;
}
