package org.example.shop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import org.example.shop.domain.Address;
import org.example.shop.domain.Member;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderStatus;
import org.example.shop.domain.item.Book;
import org.example.shop.domain.item.Item;
import org.example.shop.exception.NotEnoughStockException;
import org.example.shop.repository.MemberRepository;
import org.example.shop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  MemberRepository memberRepository;

  @Test
  public void order() throws Exception {
    //given
    Member member = createMember("user1");

    Book book = createBook("JPA!!", "kim", 10000, 10);

    int orderCount = 2;
    //when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    Order order = orderRepository.findOne(orderId);
    //then

    assertEquals("상품 주문시 주문 상태는 ORDER", OrderStatus.ORDER, order.getOrderStatus());
    assertEquals("주문한 상품 종류 수가 정확해야 함", 1, order.getOrderItems().size());
    assertEquals("주문 가격은 가격 * 주문 수량", 10000 * orderCount, order.getTotalPrice());
    assertEquals("주문 수량만큼 재고가 줄어야 한다", 8, book.getStockQuantity());

  }

  @Test(expected = NotEnoughStockException.class)
  public void 주문수량초과() throws Exception {
    //given
    Member member = createMember("user1");
    Item item = createBook("JPA!!", "kim", 10000, 10);

    //when
    int count = 11;
    orderService.order(member.getId(), item.getId(), count);

    //then
    fail("재고 수량 부족 예외 발생해야 함");
  }

  @Test
  public void 주문_취소() throws Exception {
    //given
    Member member = createMember("user1");
    Item item = createBook("JPA!!", "kim", 10000, 10);

    int count = 3;
    Long orderId = orderService.order(member.getId(), item.getId(), count);

    //when
    orderService.cancelOrder(orderId);
    Order findOrder = orderRepository.findOne(orderId);

    //then
    assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, findOrder.getOrderStatus());
    assertEquals("주문 취소시 재고는 원복", 10, item.getStockQuantity());
  }

  private Book createBook(String name, String author, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setAuthor(author);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  private Member createMember(String name) {
    Member member = new Member();
    member.setName(name);
    member.setAddress(new Address("seoul", "river", "100200"));
    em.persist(member);
    return member;
  }
}