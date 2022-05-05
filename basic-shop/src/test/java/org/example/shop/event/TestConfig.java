package org.example.shop.event;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestConfiguration
public class TestConfig {

	@Bean
	public ApplicationListener<PostPublishedEvent> postListener() {
		return event -> {
			log.info("====================================================");
			log.info("{} published!!", event.getPost());
			log.info("====================================================");
		};
	}
}
