package lab.reservation_server.repository;

import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.enums.Role;
import lab.reservation_server.dto.response.member.MemberSimpleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

  /**
   * 회원의 id를 통해서 회원의 정보를 반환
   *
   */
  @Query("select m from Member m where m.userId = :userId")
  Optional<Member> findByUserIdWithReservation(@Param("userId") String userId);

  Optional<Member> findByUserId(String userId);

  Optional<Member> findByPhoneNum(String phoneNum);

  @Modifying
  @Query("delete from Member m where m.userId = :userId")
  void deleteByUserId(@Param("userId") String userId);

  @Query("select m from Member m where m.role = :role")
  List<Member> findAllWithRole(@Param("role") Role user);
}
