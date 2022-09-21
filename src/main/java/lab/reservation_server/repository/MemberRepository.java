package lab.reservation_server.repository;

import java.util.Optional;
import lab.reservation_server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUserId(String userId);
}
