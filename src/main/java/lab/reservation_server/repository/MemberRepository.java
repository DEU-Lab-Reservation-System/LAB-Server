package lab.reservation_server.repository;

import java.util.Optional;
import lab.reservation_server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
