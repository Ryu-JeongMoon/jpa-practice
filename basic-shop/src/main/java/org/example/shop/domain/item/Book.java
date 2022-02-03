package org.example.shop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
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

}
