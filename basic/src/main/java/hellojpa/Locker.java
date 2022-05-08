package hellojpa;

import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToOne(mappedBy = "locker", fetch = LAZY)
	private Member member;
}

/*
일대일 관계는 다대일 관계와 유사하다
한쪽에서 @JoinColumn, 다른 쪽에서는 mappedBy

1 : 1 관계에서는 무엇을 기준으로 연관관계를 관리하는가
회원 - 라커, 현재를 기준으로 설계하되 변경 가능성이 큰쪽을 대비하면 좋다

하나의 회원이 여러 라커를 가질 수 있게 변경 된다면 라커 쪽에서 관리하는게 유리
하나의 라커에 여러 회원이 들어올 수 있다면 회원 쪽에서 관리하는게 유리

지연 로딩 시의 문제점
외래키 관리를 라커가 한다면 회원을 기준으로 조회할 때
라커를 지연 로딩으로 설정하더라도 Proxy 를 만들면서 값이 있는지 없는지 판단할 수 없기 때문에 즉시 로딩된다
 */