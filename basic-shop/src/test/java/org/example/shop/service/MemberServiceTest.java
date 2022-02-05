package org.example.shop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import org.example.shop.domain.Member;
import org.example.shop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  EntityManager em;

  @Test
  @Rollback(value = false)
  public void join() throws Exception {
    //given
    Member member = Member.builder()
      .name("member1")
      .build();
    //when
    Long savedId = memberService.join(member);

    assertEquals(member, memberService.findOne(savedId));
    //then

  }

  @Test(expected = IllegalStateException.class)
  public void duplicate() throws Exception {
    //given
    Member member1 = Member.builder()
      .name("ryu")
      .build();

    Member member2 = Member.builder()
      .name("ryu")
      .build();

    //when
    memberService.join(member1);
    memberService.join(member2);

    fail("예외 즉시 발생");
    //then
  }
}