package org.example.shop.repository.order.query;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

  private final EntityManager em;

  public List<OrderQueryDto> findOrderQueryDtos() {
    List<OrderQueryDto> result = findOrders();
    result.forEach(o -> o.setOrderItems(findOrderItems(o.getId())));
    return result;
  }

  private List<OrderQueryDto> findOrders() {
    return em.createQuery(
        "select " +
          "new org.example.shop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
          "from Order o " +
          "join o.member m " +
          "join o.delivery d", OrderQueryDto.class)
      .getResultList();
  }

  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery(
        "select " +
          "new org.example.shop.repository.order.query.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count) " +
          "from OrderItem oi " +
          "join oi.item i " +
          "where oi.order.id = :orderId", OrderItemQueryDto.class)
      .setParameter("orderId", orderId)
      .getResultList();
  }

  public List<OrderQueryDto> findAllByDto_optimization() {
    List<OrderQueryDto> result = findOrders();
    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));
    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));

    return result;
  }

  private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
    List<OrderItemQueryDto> orderItems =
      em.createQuery(
          "select " +
            "new org.example.shop.repository.order.query.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count) " +
            "from OrderItem oi " +
            "join oi.item i " +
            "where oi.order.id in :orderIds", OrderItemQueryDto.class)
        .setParameter("orderIds", orderIds)
        .getResultList();

    return orderItems
      .stream()
      .collect(groupingBy(OrderItemQueryDto::getOrderId));
  }

  private List<Long> toOrderIds(List<OrderQueryDto> result) {
    return result.stream()
      .map(OrderQueryDto::getId)
      .collect(toList());
  }

  public List<OrderFlatDto> findAllByDto_flat() {
    return em.createQuery(
        "select " +
          "distinct new org.example.shop.repository.order.query.OrderFlatDto(" +
          "o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count) " +
          "from Order o " +
          "join o.member m " +
          "join o.delivery d " +
          "join o.orderItems oi " +
          "join oi.item i", OrderFlatDto.class)
      .getResultList();
  }
}
