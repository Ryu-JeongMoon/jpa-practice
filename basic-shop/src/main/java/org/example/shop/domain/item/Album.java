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
@DiscriminatorValue("A")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item {

  private String artist;
  private String etc;
}
