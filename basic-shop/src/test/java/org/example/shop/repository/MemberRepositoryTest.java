package org.example.shop.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.example.shop.domain.Address;
import org.example.shop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Test
  void save() {
    Member member = new Member();
    member.setName("panda");
    member.setAddress(new Address("seoul", "hongdae", "01010"));
    memberRepository.save(member);

    List<Member> members = memberRepository.findByName("panda");
    assertThat(members.size()).isEqualTo(1);
  }

  @Test
  void findOne() {
  }

  @Test
  void findAll() {
  }

  @Test
  void findByName() {
  }
}