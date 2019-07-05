package io.github.yoshikawaa.sample.todo.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
	private String todoId;
	private String todoTitle;
	private boolean finished;
	private LocalDateTime createdAt;
}
