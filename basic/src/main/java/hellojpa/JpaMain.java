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

			em.persist(member1);
			em.persist(member2);

			em.flush();
			em.clear();

			Member referenceMember = em.getReference(Member.class, member1.getId());
			System.out.println("referenceMember = " + referenceMember);
			System.out.println("referenceMember.class = " + referenceMember.getClass());

			Member findMember = em.find(Member.class, member1.getId());
			System.out.println("findMember = " + findMember);

			System.out.println("(findMember == referenceMember) = " + (findMember == referenceMember));
			System.out.println("(findMember equals referenceMember) = " + (findMember.equals(referenceMember)));

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
엔티티에서 다른 엔티티와 연관관계를 맺고 있을 때 조회 시에 조인해서 결과를 땡겨온다
다른 엔티티를 사용하지 않을 때도 조인이 수행되기 때문에 최적화 측면에서 볼 때 매번 가져올 필요는 없다
이를 해결하기 위해 JPA 는 Proxy & Lazy Loading 사용

em.getReference() 를 통해 HibernateProxy 객체로 가져오는데 프록시 객체 초기화는 영속성 컨텍스트에 의해 처리된다
JPA 는 단일 트랜잭션 내에서 동일성을 보장하므로 em.getReference 수행 후 em.find 로 실제 데이터를 찾아와도 프록시 객체를 반환한다
프록시 객체에 값이 들어가는 것은 아니며 Proxy -> Entity 로의 참조를 가지고 위임하는 형식이다
Proxy 객체는 Entity 를 상속 받아 만들어지는 것이므로 이 것이 Entity 에서 빈 기본 생성자가 필요한 이유다

일반적으로 EntityManager 를 직접 다룰 일이 많지 않기 때문에 em.getReference() 를 사용하는 대신
Global Fetch Strategy 를 LAZY, EAGER 로 설정한다
다른 엔티티와의 관계를 고려해서 함께 사용하는 빈도가 많다면 EAGER, 적다면 LAZY 가 기본 선택 사항이다

실무를 생각해볼 때 같이 땡겨오는 일이 많더라도 가급적 지연 로딩만 사용한다
즉시 로딩도 쓰라고 만들어둔건데 왜 사용하면 안 될까?
1. 즉시 로딩을 설정해두면 예상하지 못한 SQL 발생?
2. JPQL 에서 N + 1 문제 발생

XXToOne 관계를 모두 LAZY 로 발라준다 (@ManyToOne, @OneToOne)
XXToMany 는 default LAZY 이기 때문에 그냥 둬도 된다
 */