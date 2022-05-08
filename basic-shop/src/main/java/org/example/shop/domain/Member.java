package org.example.shop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.example.shop.util.BooleanToYNConverter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String name;

	@Convert(converter = BooleanToYNConverter.class)
	private boolean active;

	@Embedded
	private Address address;

	@JsonIgnore
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

	@Builder
	public Member(String name, Address address, List<Order> orders) {
		this.name = name;
		this.address = address;
		this.orders = orders != null ? orders : this.orders;
	}
}

/*
Entity 전체에 @NoArgsConstructor(access = AccessLevel.PROTECTED) 바르고 모든 Fetch 는 LAZY 로 해놓자
JPA 가 Reflection, Proxy 등을 활용할 수 있게 기본 생성자를 열어놔야 하는데
public 으로 열어놓으면 다른 개발자가 사용할 수 있으므로 protected 라도 제한을 걸어둬야 한다
FetchType.EAGER 는 N + 1 문제를 발생 시킬 여지가 있고 이 외에도 예측하기 힘든 쿼리를 발생시킨다

DB 에서 boolean 값으로 관리될 수 있는 필드는 1, 0 으로 표현하기 보다
@Convert 를 이용해서 원하는 타입으로 명시적으로 변경될 수 있게 하거나
Enum 을 사용해 Y, N 또는 TRUE, FALSE 로 관리할 수 있다
Enum 사용 시에는 @Enumerated 사용해서 EnumType.STRING 주자

양방향 연관관계를 걸었을 땐 둘 중 하나에서 반드시 @JsonIgnore 로 출력 시에 관계를 끊어줘야 한다
끊지 않으면 양방향 필드 간 무한 호출로 StackOverFlow 뜸

EAGER 는 왜 사용하면 안 될까?
N+1 문제는 차치하고 데이터가 필요하지 않은 상황에도 끌고 오기 때문이다
프로젝트의 기본 틀이 완성된 후에는 성능 최적화를 통해 효율적인 방식으로 만들어야 하는데
EAGER 는 묻지도 따지지도 않고 그냥 땡겨온다
그 과정에서 잘못된 쿼리가 날라갈 수도 있고 N+1 문제는 뽀나스
 */