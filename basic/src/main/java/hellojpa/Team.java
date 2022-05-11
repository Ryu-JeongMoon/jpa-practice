package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "TEAM_ID")
	private Long id;

	private String name;

	// @BatchSize(size = 50)
	@OneToMany(mappedBy = "team")
	private List<Member> members = new ArrayList<>();

	@Builder
	public Team(Long id, String name, List<Member> members) {
		this.id = id;
		this.name = name;
		this.members = members != null ? members : new ArrayList<>();
	}
}

/*
Member - Team 관계에서 Team 에서 외래키 관리하는 방식은 일대다 단방향
@JoinColumn 필수적으로 넣어줘야 하고 JPA 에서 지원하고 있긴 하지만 권장되지 않는 방식이다

FK 가 다른 테이블에 있다는게 가장 큰 문제이고
그로 인해 회원을 추가할 때도 Team 테이블에 인서트 쿼리 날리고, Member 테이블에도 업데이트 쿼리가 날라간다
프로젝트가 작은 경우에는 사용할 수도 있지만 규모가 커질수록 개념적 차이가 커지므로 비추
 */