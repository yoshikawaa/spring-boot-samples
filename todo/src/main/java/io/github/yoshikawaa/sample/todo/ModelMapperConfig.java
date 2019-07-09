package io.github.yoshikawaa.sample.todo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(BeanFactory beanFactory) {
        return new ModelMapper();
    }
}
