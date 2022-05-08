package org.example.shop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.example.shop.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

	@PersistenceContext
	private EntityManager em;

	public void save(Member member) {
		em.persist(member);
	}

	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}

	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
			.getResultList();
	}

	public List<Member> findByName(String name) {
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
			.setParameter("name", name)
			.getResultList();
	}
}

/*
실무에서 merge 사용하면 안 되는 이유
dirty-checking 은 변경된 값만 DB 에 반영되는데
merge 는 값을 모두 갈아끼워 버리기 때문에 null 로 들어온 값은 DB 에 null 로 반영된다
 */