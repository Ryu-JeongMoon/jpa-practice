package jpql;

import java.util.List;
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
      Team team = new Team();
      team.setName("teamA");
      em.persist(team);

      Member member = new Member();
      member.setUsername("teamA");
      member.setAge(20);
      member.changeTeam(team);
      em.persist(member);


      // from 절 서브쿼리 사용 불가
      String query1 = "select m from (select m2 from Member m2 where m2.username = 'teamA') m";

      String query2 = "select m from Member m where exists (select t from Team t where t.name = 'teamA')";
      List<Member> members = em.createQuery(query2, Member.class)
        .getResultList();

      for (Member m : members) {
        System.out.println("m = " + m);
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
직접 애플리케이션 만들 때 entity manager 를 직접 다룰 일은 거의 없다
단 쌩 쿼리를 날려야 하는 경우에 사용할 때가 있을텐데 정식스펙이므로 getResultList(), getSingleResult() 를 사용해야 한다
Spring Data JPA 에도 이놈들이 사용되지만 내부적으로 try-catch 로 예외를 감싸서 null 또는 Optional 을 반환해준다

타입이 명확할 때, TypedQuery
TypedQuery<Member> typedQuery = em.createQuery("select m from Member as m", Member.class);

타입이 명확하지 않을 때, Query
Query rawQuery = em.createQuery("select m from Member as m");

반환값이 여러개일 때, 결과 없으면 빈 리스트 반환
typedQuery.getResultList();

반환값이 정확히 하나일 때 사용해야함
결과가 없을 때 NoResultException, 하나 이상일 때 NonUniqueResultException
rawQuery.getSingleResult();

Projection
DB 로 쿼리 날릴 때 선택하는 데이터를 의미하며 Entity / Embedded Type Value / Scala 선택 가능하다
Entity 는 본인, 엮여있는 다른 놈 선택 가능하다 단 엮여있는 놈 선택할 때 묵시적 조인이 일어난다
이는 쿼리만 놓고 봤을 때 묵시적 조인이 일어나는 것을 쉽게 파악하기 힘들기 때문에 조인은 항상 명시적으로 때려주자
노노 => select m.team from Member m
오께이 => select t from Member m join m.team t

Scala 타입으로 뽑아낼 때 그냥 값만 뽑아버리면 Object[] 로 받아서 형변환 알아서 처리하고 해줘야한다
매우 귀찮고 어떤 값을 뽑아내는지도 불분명하기 때문에 보통 DTO 형태로 뽑아낸다
package.class 로 생성자 지정해서 뽑아내야 하기 때문에 실무에서는 QueryDSL 을 더 많이 이용한다
MemberDTO memberDTO =
  em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
    .getSingleResult();

Paging
Oracle10g Dialect
select *
from
  ( select
      row_.*,
      rownum rownum_
  from
      ( select
          member0_.id as id1_0_,
          member0_.age as age2_0_,
          member0_.TEAM_ID as team_id4_0_,
          member0_.username as username3_0_
      from
          Member member0_
      left outer join
          Team team1_
              on (
                  member0_.username=team1_.name
              ) ) row_
  where
      rownum <= ?
  )
where
    rownum_ > ?

JPA Sub Query 한계
JPA 표준 스펙에서는 where, having 절에서만 sub query 가능
Hibernate 구현체에서는 select 에서도 지원
from 절에서의 서브 쿼리는 JPQL 에서 불가능
 */