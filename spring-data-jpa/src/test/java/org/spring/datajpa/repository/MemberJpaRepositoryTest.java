package org.spring.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.spring.datajpa.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

  @Autowired
  MemberJpaRepository jpaRepository;

  @Test
  void testMember() {
    Member member = new Member("userA");
    Member savedMember = jpaRepository.save(member);

    Member findMember = jpaRepository.find(savedMember.getId());

    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    assertThat(findMember).isEqualTo(member);
  }

  @Test
  void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");

    jpaRepository.save(member1);
    jpaRepository.save(member2);

    Member findMember1 = jpaRepository.findById(member1.getId()).get();
    Member findMember2 = jpaRepository.findById(member2.getId()).get();

    findMember1.setUsername("memememememem");

    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    List<Member> all = jpaRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    long count = jpaRepository.count();
    assertThat(count).isEqualTo(2);

    jpaRepository.delete(member1);
    jpaRepository.delete(member2);

    long deletedCount = jpaRepository.count();
    assertThat(deletedCount).isEqualTo(0);
  }

  @Test
  void findByUsernameBlahBlah() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userA", 20);

    jpaRepository.save(member1);
    jpaRepository.save(member2);

    List<Member> members = jpaRepository.findByUsernameAndAgeGreaterThan("userA", 15);
    assertThat(members.get(0).getUsername()).isEqualTo("userA");
    assertThat(members.get(0).getAge()).isEqualTo(20);
    assertThat(members.size()).isEqualTo(1);
  }

  @Test
  void findByNamedQuery() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userB", 20);

    jpaRepository.save(member1);
    jpaRepository.save(member2);

    List<Member> userA = jpaRepository.findByUsername("userA");
    assertThat(userA.get(0).getUsername()).isEqualTo(member1.getUsername());
  }

  @Test
  void findByPage() {
    jpaRepository.save(new Member("member1", 10));
    jpaRepository.save(new Member("member2", 10));
    jpaRepository.save(new Member("member3", 10));
    jpaRepository.save(new Member("member4", 40));
    jpaRepository.save(new Member("member5", 10));

    int age = 10;
    int offset = 0;
    int limit = 3;

    List<Member> members = jpaRepository.findByPage(age, offset, limit);
    long totalCount = jpaRepository.totalCount(10);

    assertThat(members.size()).isEqualTo(3);
    assertThat(totalCount).isEqualTo(5);
  }

  @Test
  void bulkAgePlus() {
    jpaRepository.save(new Member("member1", 15));
    jpaRepository.save(new Member("member2", 19));
    jpaRepository.save(new Member("member3", 20));
    jpaRepository.save(new Member("member4", 25));
    jpaRepository.save(new Member("member5", 330));

    int resultCount = jpaRepository.bulkAgePlus(20);

    assertThat(resultCount).isEqualTo(3);
  }
}