package org.example.shop.repository.order.simplequery;

import java.time.LocalDateTime;

import org.example.shop.domain.Address;
import org.example.shop.domain.Order;
import org.example.shop.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderSimpleQueryDto {

	private Long id;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;

	public OrderSimpleQueryDto(Order order) {
		id = order.getId();
		name = order.getMember().getName();
		orderDate = order.getOrderDate();
		orderStatus = order.getOrderStatus();
		address = order.getDelivery().getAddress();
	}

	public OrderSimpleQueryDto(Long id, String name, LocalDateTime orderDate, OrderStatus orderStatus,
		Address address) {
		this.id = id;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
	}
}