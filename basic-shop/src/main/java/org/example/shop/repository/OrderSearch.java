package org.example.shop.repository;

import lombok.Getter;
import lombok.Setter;
import org.example.shop.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {

  private String memberName;
  private OrderStatus orderStatus;
}
