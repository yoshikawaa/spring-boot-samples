package io.github.yoshikawaa.sample.todo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.dozermapper.core.Mapper;

import io.github.yoshikawaa.sample.todo.domain.CustomMapping;
import io.github.yoshikawaa.sample.todo.domain.DefaultMapping;
import io.github.yoshikawaa.sample.todo.domain.Source;

@SpringBootTest
class DozerTest {

    @Autowired
    private Mapper dozerMapper;

    @Test
    void testDefaultMapping() {
        // setup
        Source source = new Source("1", "sample todo", true, LocalDate.of(2019, 1, 1));
        // execute
        DefaultMapping destination = dozerMapper.map(source, DefaultMapping.class);
        // assert
        assertThat(destination)
            .extracting(DefaultMapping::getId, DefaultMapping::getTitle, DefaultMapping::isFinished, DefaultMapping::getCreatedAt)
            .containsExactly(source.getId(), source.getTitle(), source.isFinished(), source.getCreatedAt());
    }

    @Test
    void testCustomMapping() {
        // setup
        Source source = new Source("1", "sample todo", true, LocalDate.of(2019, 1, 1));
        // execute
        CustomMapping destination = dozerMapper.map(source, CustomMapping.class);
        // assert
        assertThat(destination)
            .extracting(CustomMapping::getTodoId, CustomMapping::getTodoTitle, CustomMapping::isFinished, CustomMapping::getCreatedAt)
            .containsExactly(source.getId(), source.getTitle(), source.isFinished(), "2019/01/01");
    }
}
