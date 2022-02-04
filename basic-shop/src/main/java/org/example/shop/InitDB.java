package org.example.shop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.shop.domain.Address;
import org.example.shop.domain.Delivery;
import org.example.shop.domain.Member;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderItem;
import org.example.shop.domain.item.Book;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;

    public void dbInit1() {

      Member member = createMember("아사다 마오", "tokyo", "japan", "10030");
      Book book1 = createBook("인간 실격", "다자이 오사무", 15000, 3000, "10101");
      Book book2 = createBook("데미안", "헤르만 헤세", 18000, 5000, "60606");

      em.persist(member);
      em.persist(book1);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, 15000, 3);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 18000, 7);

      Delivery delivery = Delivery.builder()
        .address(member.getAddress())
        .build();
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

      em.persist(order);
    }


    public void dbInit2() {
      Member member = createMember("김연아", "seoul", "korea", "30134");
      Book book1 = createBook("폭풍의 언덕", "브론테", 16000, 15000, "10103");
      Book book2 = createBook("오브젝트", "조영호", 27000, 25000, "59595");

      em.persist(member);
      em.persist(book1);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, 16000, 8);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 27000, 50);

      Delivery delivery = Delivery.builder()
        .address(member.getAddress())
        .build();
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

      em.persist(order);
    }

    private Member createMember(String name, String city, String street, String zipcode) {
      return Member.builder()
        .name(name)
        .address(new Address(city, street, zipcode))
        .build();
    }

    private Book createBook(String name, String author, int price, int stockQuantity, String isbn) {
      return Book.builder()
        .name(name)
        .price(price)
        .stockQuantity(stockQuantity)
        .author(author)
        .isbn(isbn)
        .build();
    }
  }
}

/*
@PostConstruct 에 직접 로직 넣고 @Transactional 걸면 잘 안 되는 경우가 있다
그래서 @Transactional 건 Bean 을 따로 생성하고 걔를 호출하는 식으로 만듬 스프링이 빈을 다 생성하고
@PostConstruct 를 호출하는데 라이프 사이클에 문제가 있나봄?
 */