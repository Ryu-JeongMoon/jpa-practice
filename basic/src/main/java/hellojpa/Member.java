package hellojpa;

import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ")
public class Member {

	@Id
	@Column(name = "MEMBER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
	private Long id;

	@Column(name = "name")
	private String username;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Column(name = "FOOD_NAME")
	@ElementCollection
	@CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
	private Set<String> favoriteFoods = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
	private List<Address> addressHistory = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<MemberProduct> memberProducts = new ArrayList<>();

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "LOCKER_ID")
	private Locker locker;

	@Embedded
	private Period workPeriod;

	@Embedded
	private Address homeAddress;

	@Embedded
	@AttributeOverrides(value = {
		@AttributeOverride(name = "city", column = @Column(name = "work_city")),
		@AttributeOverride(name = "street", column = @Column(name = "work_street")),
		@AttributeOverride(name = "zipcode", column = @Column(name = "work_zipcode"))
	})
	private Address workAddress;

	public Member(String username) {
		this.username = username;
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}

/*
@Entity
JPA 가 관리할 테이블임을 알려줌

@Transient
메모리에서만 사용하고 DB 에는 반영하지 않을 필드에 대해 사용
주된 용도는 애플리케이션 상에서 계산하여 캐시 데이터에 넣거나 하는 등으로 사용함

@Temporal
자바에서는 날짜 + 시간으로 퉁치지만 DB 에서는 보통 세분화되어 있음
옵션에서 날짜 / 시간 / 날짜 + 시간 선택
TemporalType.DATE / TemporalType.TIME / TemporalType.TIMESTAMP
Date 는 옛날 방식이지만 레거시 코드에서 사용하고 있을 확률이 높으므로 알아두자
자바 8 이상의 문법을 사용한다면 신경쓰지 않아도 되는 어노테이션

@Column
컬럼 이름, 길이 등 정의 가능
length => 문자에만 사용 가능하며 기본 255

@GeneratedValue
primary key 자동 생성
옵션에서 AUTO, IDENTITY, SEQUENCE, TABLE 선택 가능
AUTO - 방언에 맞게
IDENTITY - DB 에 위임
SEQUENCE - 주로 오라클에서 사용, pk 생성을 위한 sequence 객체 생성
TABLE - sequence 지원하지 않는 DB 에서 키 생성 전용 테이블을 생성 (성능 저하 이슈)

IDENTITY 전략을 사용할 경우에만 예외적으로 쓰기 지연이 일어나지 않고 바로 insert 문 나간다
영속성 컨텍스트에서 관리하기 위해 pk 를 알아야 하는데 IDENTITY 는 DB 에 위임했으므로
pk 값을 모르는 상태라서 pk 값을 알기 위해 insert 날리고
JDBC 내부적으로 insert 날리고 id 값을 받아오기 때문에 따로 select id 쿼리는 나가지 않는다
배치가 아닌 경우 하나의 트랜잭션에서 쓰기 지연이 그리 큰 성능 향상을 보기는 어렵기 때문에
IDENTITY 를 절대 사용하면 안 된다거나 하지는 않는다

DB 중심의 설계에서 벗어나 객체지향적인 설계를 위해서
MEMBER -> TEAM 참조를 가져야 한다
단 JPA 에서 관계를 알 수 있도록 @ManyToOne 등의 관계 애노테이션, @JoinColumn 사용해 관계와 외래키를 알려주도록 한다
관계 설정 시에 Fetch Strategy 선택 가능

FK 관리는 어디서 하는가
비지니스에서 중요한 쪽을 기준으로 선택하는 것이 아니다
Member : Team = N : 1
DB 에서 N인 쪽에서 FK 관리를 하고 연관관계의 주인이라 칭한다
Team 에서 관리하지 않는 이유는 Team.getMembers.add() 방식으로 회원을 추가할 때
Team 테이블이 아닌 Member 테이블로 쿼리가 날라가기 때문이다
만약 Team에서 관리하면 회원을 추가하기 위해 같은 Team 이름으로 여러 컬럼을 추가해야 한다
절대 사용하면 안 되는 방식은 아니다, 다만 개념적으로 이해하기 쉽지 않기 때문에 직관적인 방식을 사용한다

또한 성능적인 측면에서도 새로운 팀을 추가할 때
팀 테이블에 insert 문이 나가고 회원 테이블에도 FK를 추가해줘야 해서 update 문이 추가로 나가게 된다
성능과 깔끔한 설계를 위해 N인 쪽에서 관리하도록 한다

DB 에서 1인 쪽은 mappedBy 옵션으로 자신이 어떤 것과 매핑되어 있는지 명시하고
이 값은 읽기 전용으로써 JPA 가 알아서 쿼리를 치지 않는다
그럼에도 불구하고 객체지향적인 설계를 위해서는 Member, Table 두 엔티티 모두에 값을 넣어주는게 좋다
하나의 트랜잭션 안에서 회원에 팀을 넣은 후, 팀에서 회원을 조회하기 위해 컬렉션 값을 순회해보면
1차 캐시에서 가져오기 때문에 회원이 들어가있지 않은 상태다
이를 해결하기 위해서는 em.flush(), em.clear() 호출 후 사용하거나 팀에도 회원을 넣어주어야 한다
일반적으로 아래와 같은 형태의 '연관관계 편의 메서드'라 부르는 메서드에서 원자적으로 처리한다
changeTeam(Team team) {
  this.team = team;
  team.getMembers().add(this);
}

편의 메서드를 사용할 때 주의해야할 점은 setXX() 방식으로 호출하지 않아야 하는 것이다
도메인에서 setter 열어두는 것은 애초에 권장되는 방식도 아닐 뿐더러
네이밍 자체가 단순하므로 Team 만 넣어주는 것으로 오해할 수 있다
의미있는 이름을 가지도록 네이밍해서 다른 사람이 어떤 작용이 일어나는지 유추할 수 있게 작성해야 한다

양방향 매핑 시에 주의할 점은 무한 루프에 걸리는 것이다
toString, lombok, JSON 라이브러리 사용 시에 Member <-> Team 간에 서로 계속해서 호출하는 문제가 생길 수 있다
toString 사용 시에는 한쪽에서만 반환하고 매핑된 쪽에서는 반대편 엔티티를 제외하고 반환해야한다

이에 대한 간단한 해결법은
@ToString 사용 시에 onExplicitlyIncluded 혹은 of 속성을 사용해 포함할 값만 명시하고
JSON 자동 생성 방지를 위해 엔티티를 반환하지 않으면 된다
DTO 반환 시에도 JSON 자동 생성을 주의해서 양방향으로 걸려있다면 한 쪽에서는 @JsonIgnore 붙여주도록 한다
 */
