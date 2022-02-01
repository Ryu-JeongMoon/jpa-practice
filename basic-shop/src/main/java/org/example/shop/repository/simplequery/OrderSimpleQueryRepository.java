package org.example.shop.repository.simplequery;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

  private final EntityManager em;

  public List<OrderSimpleQueryDto> findOrderDtos() {
    return em.createQuery(
        "select new org.example.shop.repository.simplequery.OrderSimpleQueryDto(o.id, o.member.name, o.orderDate, o.orderStatus, d.address) from Order o join fetch o.member m join fetch o.delivery d",
        OrderSimpleQueryDto.class)
      .getResultList();
  }
}
