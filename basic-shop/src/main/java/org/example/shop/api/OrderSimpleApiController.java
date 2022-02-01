package org.example.shop.api;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.shop.domain.Address;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderStatus;
import org.example.shop.repository.OrderRepository;
import org.example.shop.repository.OrderSearch;
import org.example.shop.repository.simplequery.OrderSimpleQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * XXToOne 관계 성능 최적화 Order Order -> Member      (ManyToOne) Order -> Delivery    (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;
  private final OrderSimpleQueryRepository orderSimpleQueryRepository;

  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1() {
    return orderRepository.findAllByString(new OrderSearch());
  }

  @GetMapping("/api/v2/simple-orders")
  public Result<OrderSimpleDto> ordersV2() {
    List<OrderSimpleDto> result = orderRepository.findAllByString(new OrderSearch())
      .stream()
      .map(OrderSimpleDto::new)
      .collect(toList());
    return new Result(result);
  }

  @GetMapping("/api/v3/simple-orders")
  public Result<OrderSimpleDto> ordersV3() {
    List<OrderSimpleDto> result = orderRepository.findAllWithMemberDelivery()
      .stream()
      .map(OrderSimpleDto::new)
      .collect(toList());
    return new Result(result);
  }

  @GetMapping("/api/v4/simple-orders")
  public Result<OrderSimpleDto> ordersV4() {
    return new Result(orderSimpleQueryRepository.findOrderDtos());
  }

  @Data
  @AllArgsConstructor
  static class Result<T> {

    private T data;
  }

  @Data
  static class OrderSimpleDto {

    private Long id;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleDto(Order order) {
      id = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getOrderStatus();
      address = order.getDelivery().getAddress();
    }
  }
}
