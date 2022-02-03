package org.example.shop.domain.item;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import org.example.shop.domain.Category;
import org.example.shop.exception.NotEnoughStockException;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class Item {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private int price;

  private int stockQuantity;

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();

  public void addStock(int stockQuantity) {
    this.stockQuantity += stockQuantity;
  }

  public void removeStock(int stockQuantity) {
    int restStock = this.stockQuantity - stockQuantity;
    if (restStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }
    this.stockQuantity = restStock;
  }

}
