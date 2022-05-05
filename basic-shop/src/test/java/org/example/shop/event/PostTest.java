package org.example.shop.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@Import(TestConfig.class)
class PostTest {

	@Autowired
	PostRepository postRepository;

	@Test
	@Rollback(value = false)
	@DisplayName("이벤트 반응")
	void event() {
		// given
		Post post = Post.builder()
			.title("panda")
			.content("bear")
			.build();

		// when
		post.publish();
		postRepository.save(post);

		// then
		log.info("yahoo");
	}

}