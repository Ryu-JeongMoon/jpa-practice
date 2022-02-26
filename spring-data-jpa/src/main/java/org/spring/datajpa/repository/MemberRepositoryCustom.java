package org.spring.datajpa.repository;

import java.util.List;
import org.spring.datajpa.entity.Member;

public interface MemberRepositoryCustom {

  List<Member> findMemberCustom();
}
