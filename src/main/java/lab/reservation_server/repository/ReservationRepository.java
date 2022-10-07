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

  /**
   * 해당 강의실에 현재 시간에 이용중인 예약 내역을 반환한다.
   */
  @Query("select r from Reservation r where r.lab =:lab and r.endTime > :now and r.startTime < :now")
  Optional<List<Reservation>> findCurrentReservation(@Param("lab") Lab lab,@Param("now") LocalDateTime now);

  /**
   * 특정 강의실, 특정 시간대 범위에 이용중인 reservation을 반환한다. (오늘 기준으로 검색, 스케줄러를 통해 일주일 단위로 삭제 해도
   * 올바른 데이터 반환)
   */
  @Query("select r from Reservation r where r.lab =:lab and r.endTime > :startTime and r.startTime < :endTime and Date(r.createdDate) = :today")
  Optional<List<Reservation>> findCurrentReservationBetweenTime(@Param("lab") Lab lab,
                                                                @Param("startTime") LocalDateTime startTime,
                                                                @Param("endTime") LocalDateTime endTime,
                                                                @Param("today") java.sql.Date today);
}
