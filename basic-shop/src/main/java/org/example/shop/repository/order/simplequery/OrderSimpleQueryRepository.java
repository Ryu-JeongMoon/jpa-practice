package org.example.shop.repository.order.simplequery;

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
        "select new org.example.shop.repository.order.simplequery.OrderSimpleQueryDto(o.id, o.member.name, o.orderDate, o.orderStatus, d.address) from Order o join fetch o.member m join fetch o.delivery d",
        OrderSimpleQueryDto.class)
      .getResultList();
  }
}

/*
DTO 에 생성자를 만들어 둔 후 FQCN 을 갖다가 처박고 생성자에 데이터 꽂아준다
 */