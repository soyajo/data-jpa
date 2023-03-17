package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;

import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String userName, int age);

    // 네임드 쿼리는 실무에 사용안함.
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 오타 검증 가능
    // 실무에서 사용함.
    // 정적쿼리만 하는게 좋음. - 동적쿼리는 querydsl 로 사용해야함.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptiionalByUsername(String username); //단건 Optional

    /**
     * totalCount 쿼리 성능 이슈 해결
     * 원인 - 조인할 때 totalCount 쿼리도 조인이 되버린다.
     * 해결 @Query 를 이용하여 조인을 못하도록 막는다.
     */
    @Query(value = "select m from Member m", countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);


    // Modifying - executoUpdate() 호출 , 영속성 초기화 - clearAutomatically = true
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // fetch join 1
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // fetch join 2
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    /**
     * fetch join 3
     *
     *
     * @return
     */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //fetch join 4
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findEntityNamedGraphByUsername(@Param("username") String username);

    /**
     * 변경을 못하게 함.
     *
      */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);


    /**
     * 실시간 서비스에는 락을 걸면 안된다.
     * @param username
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
