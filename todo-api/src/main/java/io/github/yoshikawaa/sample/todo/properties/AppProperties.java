package io.github.yoshikawaa.sample.todo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("app")
@Getter
@Setter
public class AppProperties {
	private long maxUnFinishedCount;
}
