package org.example.shop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.shop.domain.Member;
import org.example.shop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public Long join(Member member) {

    validateDuplicateMember(member);
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    List<Member> findMembers = memberRepository.findByName(member.getName());

    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다");
    }
  }

  public Member findOne(Long id) {
    return memberRepository.findOne(id);
  }

  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  @Transactional
  public void update(Long id, String name) {
    Member member = memberRepository.findOne(id);
    member.setName(name);
  }
}
