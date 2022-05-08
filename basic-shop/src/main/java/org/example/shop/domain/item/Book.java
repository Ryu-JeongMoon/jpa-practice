package org.example.shop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

	private String author;
	private String isbn;

	@Builder
	public Book(String name, int price, int stockQuantity, String author, String isbn) {
		this.isbn = isbn;
		this.author = author;

		setName(name);
		setPrice(price);
		setStockQuantity(stockQuantity);
	}
}
