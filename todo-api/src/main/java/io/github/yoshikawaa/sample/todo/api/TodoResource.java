package io.github.yoshikawaa.sample.todo.api;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResource {
	private String todoId;
	@NotEmpty
	@Size(min = 1, max = 30)
	private String todoTitle;
    private boolean finished;
    private LocalDateTime createdAt;
}
