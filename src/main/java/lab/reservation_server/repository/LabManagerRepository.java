package lab.reservation_server.repository;

import java.time.LocalDate;
import java.util.Optional;
import lab.reservation_server.domain.LabManager;
import lab.reservation_server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabManagerRepository extends JpaRepository<LabManager, Long> {

    /**
     * 현재 시간과 lab id를 통해서 Member 반환
     */
    @Query("select lm.member from LabManager lm where lm.lab.id = :labId and lm.createDate = :now")
    Optional<Member> findMemberByLabId(@Param("labId") Long labId,@Param("now") LocalDate now);

}
