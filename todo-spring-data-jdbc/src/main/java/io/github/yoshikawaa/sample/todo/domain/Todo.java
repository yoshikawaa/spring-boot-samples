package io.github.yoshikawaa.sample.todo.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private String todoId;
    private String todoTitle;
    private boolean finished;
    private LocalDateTime createdAt;
}
