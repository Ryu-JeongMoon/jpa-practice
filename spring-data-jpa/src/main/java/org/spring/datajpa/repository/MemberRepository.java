package org.spring.datajpa.repository;

import java.util.List;
import javax.persistence.QueryHint;
import org.spring.datajpa.dto.MemberDto;
import org.spring.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

  List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

  // @Query 로 namedQuery 지정해줌. 근데 얘는 관례상 Member.findByUsername 으로 찾아보기 때문에 지정 안 해줘도 되긴 함 ㅋ
  // 매개변수 필요할 땐 꼭 @Param 으로 지정해줘야함, 안해주면 안 돌아가
  @Query(name = "Member.findByUsername")
  List<Member> findByUsername(@Param("username") String username);

  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username") String username, @Param("age") int age);

  @Query("select m.username from Member m")
  List<String> findUsernameList();

  @Query("select new org.spring.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
  List<MemberDto> findMemberDto();

  @Query("select m from Member m where m.username in :names")
  List<Member> findByNames(@Param("names") List<String> names);

  // sorting 조건 복잡해지면 여기서 직접 sort 해줌
  @Query("select m from Member m")
  Page<Member> findByAge(int age, Pageable pageable);

  @Query("select m from Member m")
  Slice<Member> findAllByAge(int age, Pageable pageable);

  // JPQL 사용하는 경우, parameter binding 필요할 때 @Param 사용해서 지정해줘야 함
  @Modifying(clearAutomatically = true) // 얘를 붙여줘야 executeUpdate 됨
  @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);

  @Query("select m from Member m left join fetch m.team t")
  List<Member> findMemberFetchJoin();

  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();

  @EntityGraph(attributePaths = {"team"})
  @Query("select m from Member m")
  List<Member> findMemberEntityByUsername();

  @EntityGraph("Member.all")
  List<Member> findEntityGraphByUsername(@Param("username") String username);

  @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
  Member findReadOnlyByUsername(String username);

  List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
}
