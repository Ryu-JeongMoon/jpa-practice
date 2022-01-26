package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();

    try {
      Member member1 = new Member("member4");
      Member member2 = new Member("member5");
      Member member3 = new Member("member6");

      System.out.println("member1.getId() = " + member1.getId());
      System.out.println("member2.getId() = " + member2.getId());
      System.out.println("member3.getId() = " + member3.getId());

      em.persist(member1);
      em.persist(member2);
      em.persist(member3);

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}
