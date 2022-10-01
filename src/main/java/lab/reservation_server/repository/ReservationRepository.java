package lab.reservation_server.repository;

import java.util.Optional;
import lab.reservation_server.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId")
  Optional<Reservation> findReservationByMemberId(@Param("memberId") Long memberId);
}
