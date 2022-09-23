package lab.reservation_server.repository;

import java.util.Optional;
import lab.reservation_server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

  @Query("select m from Member m left join fetch m.reservation where m.userId = :userId")
  Optional<Member> findByUserId(@Param("userId") String userId);
}
