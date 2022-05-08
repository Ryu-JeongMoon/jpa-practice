package hellojpa;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

	private String createdBy;
	private String lastModifiedBy;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
}

/*
BaseEntity 는 엔티티가 아님, 테이블이 생성되지 않으므로 다형성을 이용한 검색은 당연히 안 되고
속성 정보만 내려주기 위해 존재하니까 추상 클래스로 만들어두는 것이 좋다

자바는 다중상속 지원 안 하니까 상속 한번만 받을 수 있는데 중요한 상속 기회를 MappedSuperclass 에 써야 할까?
그렇다 MappedSuperclass 는 interface 지원하지 않는다
JPA has no direct support for interfaces or variable relationships.
 */