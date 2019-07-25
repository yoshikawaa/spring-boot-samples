package io.github.yoshikawaa.sample.todo.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMapping {
    private String id;
    private String title;
    private boolean finished;
    private LocalDate createdAt;
}
