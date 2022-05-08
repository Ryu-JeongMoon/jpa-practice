package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class NativeQueryMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {
			Member member1 = new Member("panda");
			em.persist(member1);

			em.flush();
			em.clear();

			List<Member> members = em.createNativeQuery("select * from Member", Member.class).getResultList();
			for (Member member : members) {
				System.out.println("member = " + member);
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}

/*
native query 함께 사용 시에 em.flush()를 적절히 호출해줘야 한다
jpql 사용할 때는 조회하기 전 알아서 날라가지만 네이티브 쿼리 사용 시, 자동으로 날라가지 않는다
 */