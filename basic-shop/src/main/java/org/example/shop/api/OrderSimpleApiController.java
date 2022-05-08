package org.example.shop.api;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import org.example.shop.domain.Address;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderStatus;
import org.example.shop.repository.OrderRepository;
import org.example.shop.repository.OrderSearch;
import org.example.shop.repository.order.simplequery.OrderSimpleQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * XXToOne 관계 성능 최적화 Order Order -> Member      (ManyToOne) Order -> Delivery    (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	/**
	 * v1. List<Entity> 반환<br/>
	 * API 는 스펙이기 때문에 변화 가능성이 큰 엔티티를 직접 반환해서는 안 된다<br/>
	 * 또한 엔티티 직접 반환은 N+1 발생 가능성이 있다
	 */
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());

		// 강제 초기화를 위한 순회
		for (Order order : orders) {
			order.getMember().getName();
			order.getDelivery().getAddress();
		}
		return orders;
	}

	/**
	 * v2. DTO 반환<br/>
	 * N+1 문제 발생하여 성능 저하
	 */
	@GetMapping("/api/v2/simple-orders")
	public Result<OrderSimpleDto> ordersV2() {
		List<OrderSimpleDto> result = orderRepository.findAllByString(new OrderSearch())
			.stream()
			.map(OrderSimpleDto::new)
			.collect(toList());
		return new Result(result);
	}

	/**
	 * v3. Fetch Join 활용한 DTO 반환<br/>
	 * 성능 냠냠굿
	 */
	@GetMapping("/api/v3/simple-orders")
	public Result<OrderSimpleDto> ordersV3() {
		List<OrderSimpleDto> result = orderRepository.findAllWithMemberDelivery()
			.stream()
			.map(OrderSimpleDto::new)
			.collect(toList());
		return new Result(result);
	}

	/**
	 * v4. DTO 직접 조회<br/>
	 * 엔티티 조회해와서 매핑하는 작업을 줄이기 위해 필요한 필드만 가져오는 선택<br/>
	 * 스펙이 굳어지는 단점이 있다, 재사용성 저하<br/><br/>
	 * Repository 에서 직접 DTO 는 물리적으로만 계층을 나눠 뒀을 뿐 API 스펙이 침투한 것이다<br/>
	 * 영한님은 DTO 조회용 Repository 를 따로 만들어 패키지 자체를 분리해두는 방식으로 해결한다<br/>
	 */
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
