package lab.reservation_server.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  /**
   * 오늘 날짜 기준, Member가 가장 최근에 예약한 Reservation을 가져온다.
   */
  @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId order by r.endTime desc")
  Optional<List<Reservation>> findReservationByMemberId(@Param("memberId") Long memberId);

  @Query("select r from Reservation r where r.lab =:lab and r.endTime > :now and r.startTime < :now")
  Optional<List<Reservation>> findCurrentReservation(@Param("lab") Lab lab,@Param("now") LocalDateTime now);
}
