package hellojpa;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  private String city;

  private String street;

  private String zipcode;
}

/*
왜 Embedded 값 타입을 따로 빼서 써야할까?
DB 에서는 하나의 테이블에 모두 들어가는 값들이지만
자바에서는 객체지향적 설계를 고려해 따로 빼내면 장점이 많다

관심사의 분리 측면에서 응집성 있는 코드 작성이 가능하며
주소 / 기간 등의 일반화하기 쉬운 값 타입을 만들어두면 재사용성이 높아질 수 있다
 */