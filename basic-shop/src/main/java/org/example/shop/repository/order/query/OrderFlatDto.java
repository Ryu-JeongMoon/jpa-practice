package org.example.shop.repository.order.query;

import java.time.LocalDateTime;

import org.example.shop.domain.Address;
import org.example.shop.domain.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderFlatDto {

	private Long id;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;

	private String itemName;
	private int orderPrice;
	private int count;
}
