package org.example.shop.api;

import static java.util.stream.Collectors.*;
import static org.example.shop.api.OrderSimpleApiController.*;

import java.time.LocalDateTime;
import java.util.List;

import org.example.shop.domain.Address;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderItem;
import org.example.shop.domain.OrderStatus;
import org.example.shop.repository.OrderRepository;
import org.example.shop.repository.OrderSearch;
import org.example.shop.repository.order.query.OrderFlatDto;
import org.example.shop.repository.order.query.OrderItemQueryDto;
import org.example.shop.repository.order.query.OrderQueryDto;
import org.example.shop.repository.order.query.OrderQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

	/**
	 * 총 쿼리 5방 나간다 개 같은 API 로써 이거 반환하면 큰일남<br/>
	 * Order -> Member -> Delivery -> OrderItem -> Item
	 */
	@GetMapping("/api/v2/orders")
	public OrderSimpleApiController.Result<OrderDto> ordersV2() {
		List<OrderDto> result = orderRepository.findAllByString(new OrderSearch())
			.stream()
			.map(OrderDto::new)
			.collect(toList());

		return new Result(result);
	}

	/**
	 * Fetch Join + distinct 로 해결<br/>
	 * 쿼리 날릴 때, 1:N 조인 걸면 N만큼 데이터 뻥튀기 된다<br/>
	 * DB 에서 distinct 는 완전히 동일한 데이터만 걸러주지만<br/>
	 * JPQL 에서의 distinct 는 Application Level 에서 같은 ID 를 가진 애를 걸러줘서 반환<br/><br/>
	 *
	 * Fetch Join 사용 시 Paging 불가<br/>
	 * 1:N 조인 거는 순간 데이터 뻥튀기 되고 DB 자체에서는 뻥튀기 된 데이터를 반환한다<br/>
	 * 따라서 Hibernate 는 경고 로그 띄우고 메모리에서 페이징 처리해버린다<br/>
	 * 데이터 개수가 얼마 되지 않는다면 상관 없지만 데이터가 커질수록 페이징 처리 때문에<br/>
	 * OutOfMemory 뜰 수 있음
	 */
	@GetMapping("/api/v3/orders")
	public OrderSimpleApiController.Result<OrderDto> ordersV3() {
		List<OrderDto> result = orderRepository.findAllWithItem()
			.stream()
			.map(OrderDto::new)
			.collect(toList());
		return new Result(result);
	}

	/**
	 * Collection Paging 하고 싶다면?<br/>
	 * XXToOne 관계는 데이터 뻥튀기가 없으므로 기냥 fetch join 갈겨버림<br/>
	 * Collection 은 Lazy Loading 으로 조회하고 hibernate option 을 사용한다<br/>
	 * default_batch_fetch_size 로 개수 지정해주고 in 절로 찍어서 가져오는 것으로 해결<br/><br/>
	 *
	 * 그렇다면 개수는 어떤 기준으로 정해야 하는가?<br/>
	 * 1000 이상으로 많아지면 WAS, DB 에 순간 부하가 심해진다<br/>
	 * 100 정도를 유지하면 큰 데이터를 가져올 때 느릴 수 있다<br/>
	 * 또한 쿼리 여러방 날리기 때문에 성능 저하가 올 수 있다<br/>
	 * 애매하면 100 ~ 500 사이로 두고 사용하자
	 */
	@GetMapping("/api/v3.1/orders")
	public OrderSimpleApiController.Result<OrderDto> ordersV3_page(
		@RequestParam(value = "offset", defaultValue = "0") int offset,
		@RequestParam(value = "limit", defaultValue = "100") int limit) {
		List<OrderDto> result = orderRepository.findAllWithMemberDelivery(offset, limit)
			.stream()
			.map(OrderDto::new)
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
	 * OrderFlatDto -> OrderQueryDto 직접 매핑, 어질어질하구만
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

/*
Entity 반환하지 말라는 의미는 Wrapper 로 한번 싸서 맥이는게 아니고
Entity 와의 관계를 완전히 끊어내라는 것이다
즉 OrderDTO 반환 시 OrderItem 을 그냥 반환해서는 안 되고
얘도 DTO 형태로 만들어 반환해야 한다

API 개발 고급 정리
v1, Entity 직접 던짐
v2, Entity -> DTO 매핑 후 던짐
v3, Fetch Join 으로 필요한 연관 관계 땡겨온 후 던짐, Fetch Join 때릴 시 페이징 불가, 컬렉션은 한번만 땡길 수 있다는 단점 존재
v3.1, ToOne 관계인 애들은 Fetch Join 때리고 컬렉션은 지연 로딩 후 default_batch_fetch_size 이용해 직접 끊어서 가져오기
v4, JPA 에서 직접 DTO 조회, 컬렉션은 조회 후 땡겨와야됨
v5, in 절을 활용해 미리 다 땡겨온 후, 애플리케이션에서 컬렉션 이용해 값 채워줌
v6, Flat 한 데이터를 받아와서 얘를 직접 조립, 쿼리 수는 줄어들지만 Memory 사용 증가

v6는 1:N 에서 1을 기준으로 페이징이 불가하고
직접 조립해야 하기 때문에 코드가 더러워진다는 단점이 있다
유일한 장점은 쿼리가 한번 나간다는 것인데 데이터 양이 많아질 수록
v5와 비교하여 성능이 떨어지게 될 수도 있다 (중복 데이터 전송이 많아지기 때문)

v5는 in 절로 땡겨온 걸 메모리에서 난리 부르스를 추면서 DTO 에 채워주는 것인데
애초에 Entity 로 조회했다면 default_batch_fetch_size 가 최적화 해주는 일이다

그렇다면 Entity 직접 조회 + default_batch_fetch_size 가 제일 좋은 것인가?
데이터 양이 많다면 Entity <-> DTO 매핑 작업도 많아지기 때문에
v5가 일반적으로 제일 나은 선택일 것으로 보인다
 */
