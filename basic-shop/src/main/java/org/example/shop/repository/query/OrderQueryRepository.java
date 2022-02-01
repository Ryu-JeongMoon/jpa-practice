package org.example.shop.repository.query;

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

    result.forEach(o -> {
      findOrderItems(o.getId());
    });

    return result;
  }

  private List<OrderItemQueryDto> findOrderItems(Long id) {
    return em.createQuery(
        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count) "
          +
          "from OrderItem oi " +
          "join oi.item i " +
          "where oi.order.id = :orderId", OrderItemQueryDto.class)
      .setParameter("orderId", id)
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
          "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count) "
            +
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
      .map(o -> o.getId())
      .collect(toList());
  }

  private List<OrderQueryDto> findOrders() {
    return em.createQuery(
        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) "
          +
          "from Order o " +
          "join o.member m " +
          "join o.delivery d", OrderQueryDto.class)
      .getResultList();
  }

  public List<OrderFlatDto> findAllByDto_flat() {
    return em.createQuery(
        "select distinct new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count) "
          +
          "from Order o " +
          "join o.member m " +
          "join o.delivery d " +
          "join o.orderItems oi " +
          "join oi.item i", OrderFlatDto.class)
      .getResultList();
  }
}
