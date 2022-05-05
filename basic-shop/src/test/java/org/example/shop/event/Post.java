package org.example.shop.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.domain.AbstractAggregateRoot;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends AbstractAggregateRoot<Post> {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private String content;

	@Builder
	public Post(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void publish() {
		this.registerEvent(new PostPublishedEvent(this));
	}
}
