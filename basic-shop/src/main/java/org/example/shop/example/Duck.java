package org.example.shop.example;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@EntityListeners(value = DuckListener.class)
public class Duck {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @PrePersist
  public void prePersist() {
    System.out.println("Duck.perPersist id = " + id);
  }

  @PostPersist
  public void postPersist() {
    System.out.println("Duck.postPersist id = " + id);
  }

  @PreUpdate
  public void preUpdate() {
    System.out.println("Duck.preUpdate");
  }

  @PostUpdate
  public void postUpdate() {
    System.out.println("Duck.postUpdate");
  }

  @PostLoad
  public void postLoad() {
    System.out.println("Duck.postLoad");
  }

  @PreRemove
  public void preRemove() {
    System.out.println("Duck.preRemove");
  }

  @PostRemove
  public void postRemove() {
    System.out.println("Duck.postRemove");
  }
}

/*
모든 엔티티를 대상으로 특정 메서드 수행 이후 로그를 찍어야 하는 경우가 있다면 ?
일반적으로 운영 상황에서 정말 필요한 기능들로써 누가 추가 / 수정 / 삭제 요청했고, 언제 요청되었는지 등
메서드 수행 이전 / 이후는 JPA Event 를 통해 알 수 있고
특정 시점에 알맞게 Listener 를 등록하거나 직접 어노테이션을 활용해 수행할 수 있다

JPA Event 발생 종류
1. PostLoad
2. PrePersist
3. PreUpdate
4. PreRemove
5. PostPersist
6. PostUpdate
7. PostRemove
 */