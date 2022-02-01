package hellojpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CriteriaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();

    try {

      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Member> query = cb.createQuery(Member.class);

      Root<Member> m = query.from(Member.class);
      CriteriaQuery<Member> cq = query.select(m);

      String username = "panda";
      if (username != null) {
        cq = cq.where(cb.equal(m.get("username"), "panda"));
      }

      List<Member> resultList = em.createQuery(cq).getResultList();

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
JPQL 은 그냥 문자열이라는 한계 때문에 동적 쿼리 작성이 매우 힘들다
이를 해결하기 위해 Criteria 를 추가하여 자바코드로 SQL 작성이 가능해졌지만
코드 가독성이 구데기라 실제 운영에 사용하면 유지보수가 극악이 된다
얘는 거의 안 쓰고 QueryDSL 사용한다
 */