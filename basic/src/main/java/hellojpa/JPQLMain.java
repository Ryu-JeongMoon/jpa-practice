package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPQLMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();

    try {

      List<Member> members = em.createQuery("select m from Member m where m.username like 'panda'", Member.class).getResultList();
      for (Member member : members) {
        System.out.println("member = " + member);
      }

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
      emf.close();
    }
  }
}

/*
JPQL, Java Persistence Query Language
객체지향 쿼리 언어

JPQL 은 왜 쓰는 것인가
열심히 애노테이션으로 객체 - 테이블 매핑한 후에 결국 보내는 것이 SQL 이라면 여전히 벤더 종속적이 된다
이 종속성을 끊어내기 위해 SQL 을 추상화한 JPQL 을 사용해 쿼리를 작성하고
JPA 가 JPQL -> SQL 파싱 & 번역할 때 dialect 인식하고 벤더 특화 쿼리로 바꿔준다
즉 JPA API 는 JPQL 로 만든 후 SQL 번역 과정을 처리해준다

MyBatis, JDBC API 등과 함께 사용할 수도 있다
단 이 때 주의점으로 JPA 를 우회해서 DB connection 을 직접 얻어와서 쿼리 날리는 경우에는
Persistence Context 관리를 못 받으니까 단일 쿼리를 날리는 상황이 아니면
em.flush() 로 JPA 로 작성한 직전 코드 반영한 후 쿼리 날려야 올바른 결과를 얻어올 수 있다
 */