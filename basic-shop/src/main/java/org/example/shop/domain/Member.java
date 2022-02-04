package org.example.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @Embedded
  private Address address;

  @JsonIgnore
  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

  @Builder
  public Member(String name, Address address, List<Order> orders) {
    this.name = name;
    this.address = address;
    this.orders = orders;
  }
}

/*
Entity 전체에 @NoArgsConstructor(access = AccessLevel.PROTECTED) 바르고 모든 Fetch 는 LAZY 로 해놓자
JPA 가 Reflection, Proxy 등을 활용할 수 있게 기본 생성자를 열어놔야 하는데
public 으로 열어놓으면 다른 개발자가 사용할 수 있으므로 protected 라도 제한을 걸어둬야 한다
FetchType.EAGER 는 N + 1 문제를 발생 시킬 여지가 있고 이 외에도 예측하기 힘든 쿼리를 발생시킨다
 */