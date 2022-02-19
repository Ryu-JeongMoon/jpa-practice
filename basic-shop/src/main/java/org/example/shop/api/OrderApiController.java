package org.example.shop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.example.shop.api.OrderSimpleApiController.Result;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.shop.domain.Address;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderItem;
import org.example.shop.domain.OrderStatus;
import org.example.shop.repository.OrderRepository;
import org.example.shop.repository.OrderSearch;
import org.example.shop.repository.query.OrderFlatDto;
import org.example.shop.repository.query.OrderItemQueryDto;
import org.example.shop.repository.query.OrderQueryDto;
import org.example.shop.repository.query.OrderQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @GetMapping("/api/v1/orders")
  public OrderSimpleApiController.Result<Order> ordersV1() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());

    orders.forEach(order -> {
      order.getMember().getName();
      order.getDelivery().getAddress();
      order.getOrderItems()
        .forEach(orderItem -> orderItem.getItem().getName());
    });

    return new Result(orders);
  }

  @GetMapping("/api/v2/orders")
  public OrderSimpleApiController.Result<OrderDto> ordersV2() {
    List<OrderDto> result = orderRepository.findAllByString(new OrderSearch())
      .stream()
      .map(o -> new OrderDto(o))
      .collect(toList());

    return new Result(result);
  }

  @GetMapping("/api/v3/orders")
  public OrderSimpleApiController.Result<OrderDto> ordersV3() {
    List<OrderDto> result = orderRepository.findAllWithItem()
      .stream()
      .map(o -> new OrderDto(o))
      .collect(toList());
    return new Result(result);
  }

  @GetMapping("/api/v3.1/orders")
  public OrderSimpleApiController.Result<OrderDto> ordersV3_page(
    @RequestParam(value = "offset", defaultValue = "0") int offset,
    @RequestParam(value = "limit", defaultValue = "100") int limit) {
    List<OrderDto> result = orderRepository.findAllWithMemberDelivery(offset, limit)
      .stream()
      .map(o -> new OrderDto(o))
      .collect(toList());
    return new Result(result);
  }

  @GetMapping("/api/v4/orders")
  public Result<OrderQueryDto> ordersV4() {
    return new Result(orderQueryRepository.findOrderQueryDtos());
  }

  @GetMapping("/api/v5/orders")
  public Result<OrderQueryDto> ordersV5() {
    return new Result(orderQueryRepository.findAllByDto_optimization());
  }

  /**
   * 어질어질하구만
   *
   * @return
   */
  @GetMapping("/api/v6/orders")
  public Result<OrderQueryDto> ordersV6() {
    List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

    List<OrderQueryDto> result = flats.stream()
      .collect(groupingBy(
        o -> new OrderQueryDto(o.getId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
        mapping(o -> new OrderItemQueryDto(o.getId(), o.getItemName(), o.getOrderPrice(), o.getCount()),
          toList())))
      .entrySet()
      .stream()
      .map(e -> new OrderQueryDto(e.getKey().getId(), e.getKey().getName(), e.getKey().getOrderDate(),
        e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
      .collect(toList());

    return new Result(result);
  }

  @Data
  static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getOrderStatus();
      address = order.getDelivery().getAddress();
      orderItems = order.getOrderItems()
        .stream()
        .map(OrderItemDto::new)
        .collect(toList());
    }
  }

  @Getter
  static class OrderItemDto {

    private final String itemName;
    private final int orderPrice;
    private final int count;

    public OrderItemDto(OrderItem orderItem) {
      itemName = orderItem.getItem().getName();
      orderPrice = orderItem.getOrderPrice();
      count = orderItem.getCount();
    }
  }
}

/**
 * 강제 초기화 양방향의 경우 한 쪽에는 @JsonIgnore 해줘야함
 */
