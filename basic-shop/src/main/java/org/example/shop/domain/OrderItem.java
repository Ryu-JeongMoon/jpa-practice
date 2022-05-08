package org.example.shop.domain;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.example.shop.domain.item.Item;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@JsonIgnore
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice;
	private int count;

	//==생성 메서드==//
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);

		item.removeStock(count);
		return orderItem;
	}

	//==비지니스 로직==//
	public void cancel() {
		getItem().addStock(count);
	}

	//==조회 로직==//
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
}

/*
@ManyToMany 를 실제 업무에 사용하는 것을 지양하자
어차피 조인 테이블 생성되는 것은 같으나 ManyToMany 매핑에 의한 테이블은 다른 컬럼 추가가 불가능하다
작은 프로젝트라면 써도 되겠지만 현업에서는 테이블에 아이디 컬럼 2개만 딸랑 두는 경우는 없다
운영을 위해 등록 / 수정 관련 정보가 필요하기 때문
 */