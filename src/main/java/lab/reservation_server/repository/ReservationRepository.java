package lab.reservation_server.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId")
  Optional<Reservation> findReservationByMemberId(@Param("memberId") Long memberId);

  @Query("select r from Reservation r where r.lab =:lab and r.endTime > :now and r.startTime < :now")
  Optional<List<Reservation>> findCurrentReservation(@Param("lab") Lab lab,@Param("now") LocalDateTime now);
}
