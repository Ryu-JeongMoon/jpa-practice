package org.spring.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.spring.datajpa.dto.MemberDto;
import org.spring.datajpa.entity.Member;
import org.spring.datajpa.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TeamRepository teamRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  void test() {
    Member member = new Member("userB");
    Member savedMember = memberRepository.save(member);

    Member findMember = memberRepository.findById(savedMember.getId()).get();

    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    assertThat(findMember).isEqualTo(member);
  }

  @Test
  void basicCRUD() {
    System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");

    memberRepository.save(member1);
    memberRepository.save(member2);

    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();

    findMember1.setUsername("memememememem");

    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    List<Member> all = memberRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    long count = memberRepository.count();
    assertThat(count).isEqualTo(2);

    memberRepository.delete(member1);
    memberRepository.delete(member2);

    long deletedCount = memberRepository.count();
    assertThat(deletedCount).isEqualTo(0);
  }

  @Test
  void findByUsernameBlahBlah() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userA", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("userA", 15);
    assertThat(members.get(0).getUsername()).isEqualTo("userA");
    assertThat(members.get(0).getAge()).isEqualTo(20);
    assertThat(members.size()).isEqualTo(1);
  }

  @Test
  void findByUsername() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> userA = memberRepository.findByUsername("userA");
    assertThat(userA.get(0).getUsername()).isEqualTo(member1.getUsername());
  }

  @Test
  void findUser() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> userA = memberRepository.findUser("userA", 10);
    assertThat(userA.get(0).getUsername()).isEqualTo(member1.getUsername());
  }

  @Test
  void findUsernameList() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<String> usernameList = memberRepository.findUsernameList();
    for (String username : usernameList) {
      System.out.println("username = " + username);
    }
  }

  @Test
  void findMemberDto() {
    Team team = new Team("teamA");
    teamRepository.save(team);

    Member member1 = new Member("AAA", 10);
    member1.setTeam(team);
    memberRepository.save(member1);

    Member member2 = new Member("BBB", 20);
    member2.setTeam(team);
    memberRepository.save(member2);

    List<MemberDto> memberDto = memberRepository.findMemberDto();
    for (MemberDto dto : memberDto) {
      System.out.println("dto = " + dto);
    }
  }

  @Test
  void findByNames() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("ABB", 10);
    Member member3 = new Member("ABC", 10);
    Member member4 = new Member("ACB", 10);
    Member member5 = new Member("BBB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
    memberRepository.save(member4);
    memberRepository.save(member5);

    List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
    for (Member byName : byNames) {
      log.info("Names = {}", byName);
    }
  }

  @Test
  void findByPage() {
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 40));
    memberRepository.save(new Member("member5", 10));

    int age = 10;

    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

    Page<Member> page = memberRepository.findByAge(age, pageRequest);
    Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

    for (MemberDto memberDto : map) {
      System.out.println("memberDto = " + memberDto);
    }

    List<Member> content = page.getContent();
    long totalElements = page.getTotalElements();

    assertThat(content.size()).isEqualTo(3);
    assertThat(totalElements).isEqualTo(5);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.isFirst()).isTrue();
    assertThat(page.hasNext()).isTrue();
  }

  @Test
  void findBySlice() {
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 40));
    memberRepository.save(new Member("member5", 10));

    int age = 10;

    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

    Slice<Member> page = memberRepository.findAllByAge(age, pageRequest);
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

//        for (MemberDto memberDto : map) {
//            System.out.println("memberDto = " + memberDto);
//        }

    List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

    assertThat(content.size()).isEqualTo(3);
//        assertThat(totalElements).isEqualTo(5);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.isFirst()).isTrue();
    assertThat(page.hasNext()).isTrue();
  }

  @Test
  void bulkAgePlus() {
    memberRepository.save(new Member("member1", 15));
    memberRepository.save(new Member("member2", 19));
    memberRepository.save(new Member("member3", 20));
    memberRepository.save(new Member("member4", 25));
    memberRepository.save(new Member("member5", 330));

    int resultCount = memberRepository.bulkAgePlus(20);

    //bulk 연산 이후에 추가 로직이 있다면 영속성 컨텍스트 날려줘야 함
    //flush / clear 사용하던지 @Modifying 에다가 clearAutomatically = true 옵션 달아주기

    // em.flush();
    // em.clear();

    List<Member> result = memberRepository.findByUsername("member5");
    Member member5 = result.get(0);

    System.out.println("member5.getAge() = " + member5.getAge());

    assertThat(resultCount).isEqualTo(3);
  }

  @Test
  void findAllMemberLazy() {
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 10, teamB);
    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    List<Member> result = memberRepository.findAll();
    for (Member member : result) {
      System.out.println("member.getUsername() = " + member.getUsername());
      System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
      System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
    }
  }

  @Test
  void findEntityGraphMemberLazy() {
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 10, teamB);
    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    List<Member> result = memberRepository.findMemberFetchJoin();
    for (Member member : result) {
      System.out.println("member.getUsername() = " + member.getUsername());
      System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
      System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
    }
  }

  @Test
  void queryHint() {
    Member member = new Member("userA", 10);
    memberRepository.save(member);

    em.flush();
    em.clear();

    Member findMember = memberRepository.findReadOnlyByUsername("userA");
    findMember.setUsername("userB");

    em.flush();
  }

  @Test
  void custom() {
    Member member1 = new Member("userA", 10);
    Member member2 = new Member("userA", 10);
    Member member3 = new Member("userA", 10);
    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);

    List<Member> memberCustom = memberRepository.findMemberCustom();
    for (Member member : memberCustom) {
      System.out.println("member.getUsername() = " + member.getUsername());
    }
  }

  @Test
  void projections() {

    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();

    List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

    for (UsernameOnly usernameOnly : result) {
      System.out.println("usernameOnly = " + usernameOnly);
    }
  }
}