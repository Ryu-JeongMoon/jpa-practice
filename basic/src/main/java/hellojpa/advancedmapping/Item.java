package hellojpa.advancedmapping;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "CATEGORY")
@Inheritance(strategy = InheritanceType.JOINED)
public class Item {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private int price;
}

/*
SINGLE_TABLE
비지니스에서 엄청 중요한 테이블이 아닐 때, 간단한 설계를 위해서 사용
하나의 테이블에 모두 때려박기 때문에 성능은 제일 좋음
단 사용하지 않는 컬럼에 null 이 들어가는 단점이 있음

TABLE_PER_CLASS
얘는 적폐임, 쓰면 안 됨
모든 테이블이 분리되기 때문에 @DiscriminatorColumn 사용할 필요가 없음
단 Item 을 추상 클래스로 만들어 테이블이 만들어지지 않게 하는데
자바에서는 리스코프 치환 원칙으로 인해 부모 타입으로도 조회가 가능해야 한다
이 때 Item 을 기준으로 찾게 되면 Item 상속 받는 모든 테이블에 union 걸고 찾아오게 된다

JOINED
상위 테이블에 하위 테이블들을 구분하기 위한 DTYPE 이 필요
필수는 아니지만 운영 시에 여러 부가 처리를 위해 DTYPE 이 있는 편이 좋다
default 로 Entity 이름이 들어가는데 하위 클래스에서 @DiscriminatorValue 로 들어가는 값을 바꿀 수 있다
테이블 정규화가 되고 저장 공간이 효율화된다
단점으로는 조회 시 조인을 많이 사용한다, Insert 할 때 상위/하위 두 테이블에 들어간다
설계 상 제일 깔끔하지만 단일 테이블 전략에 비해 복잡하다

기본적으로 JOINED 선택을 염두에 두고
단시간 사용하거나, 장시간 사용하더라도 비지니스에서 중요하지 않을 때는 SINGLE_TABLE 사용하자
단순하게 풀어낼 수 있는 것을 원리원칙 따져서 복잡하게 들어가는 것은 좋지 않다
 */