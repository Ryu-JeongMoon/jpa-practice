package org.example.shop.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class PostPublishedEvent extends ApplicationEvent {

	private final Post post;

	public PostPublishedEvent(Object source) {
		super(source);
		this.post = (Post)source;
	}
}
