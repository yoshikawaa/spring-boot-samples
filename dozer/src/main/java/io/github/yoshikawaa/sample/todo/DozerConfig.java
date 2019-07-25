package io.github.yoshikawaa.sample.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dozermapper.core.loader.api.BeanMappingBuilder;

import io.github.yoshikawaa.sample.todo.domain.CustomMapping;
import io.github.yoshikawaa.sample.todo.domain.Source;

@Configuration
public class DozerConfig {

    @Bean
    public BeanMappingBuilder mappingDestinationTodo() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(type(Source.class), type(CustomMapping.class))
                    .fields(field("id"), field("todoId"))
                    .fields(field("title"), field("todoTitle"));
            }
        };
    }
}
