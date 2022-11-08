package lab.reservation_server.repository;

import java.time.LocalDate;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.LabManager;
import lab.reservation_server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabManagerRepository extends JpaRepository<LabManager, Long> {

    /**
     * 오늘 날짜와 lab id를 통해서 해당 강의실에서 방장을 담당하고 있는 Member 반환
     */
    @Query("select m from LabManager lm join lm.member m where lm.lab.id = :labId and lm.createDate = :now")
    Optional<Member> findMemberByLabId(@Param("labId") Long labId,@Param("now") LocalDate now);

    /**
     * lab id와 날짜를 통해서 해당 강의실에서 방장을 담당하고 있는 Lab Manager 반환
     */
    @Query("select lm from LabManager lm where lm.lab = :lab and lm.createDate = :today")
    Optional<LabManager> findLabManagerByLabIdAndDate(@Param("lab") Lab lab, @Param("today") LocalDate today);
}
