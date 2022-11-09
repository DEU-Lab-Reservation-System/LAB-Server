package lab.reservation_server.repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  /**
   * 오늘 날짜 기준, Member가 가장 최근에 예약한 이상적인 Reservation을 가져온다.
   */
//  @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId order by r.endTime desc")
  @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId and r.endTime > :now order by r.startTime asc")
  Optional<List<Reservation>> findReservationByMemberId(@Param("memberId") Long memberId,
                                                        @Param("now") LocalDateTime now);

  /**
   * 특정 사용자 예약 목록 중에서 ture, false 예약 내역 중에서 제일 최근내역을 가져온다.
   */
  @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId and r.permission = :permission order by r.endTime desc")
  Optional<List<Reservation>> findApprovedReservationByMemberId(@Param("memberId") Long memberId, @Param("permission") Boolean permission);



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



  /**
   * 특정 시간대에 이용중인 예약 내역의 개수를 반환한다.
   */
  @Query("select count(r) from Reservation r where r.lab =:lab and r.endTime > :startTime and r.startTime < :endTime")
  Integer countByLabAndStartTimeBetweenEndTime(@Param("lab") Lab lab, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


  /**
   * 오늘 날짜 기준으로 특정 강의실의 17시 이후에 대한 예약 내역 개수를 반환한다.
   */
    @Query("select count(r) from Reservation r join r.lab l where r.permission = false and l.roomNumber = :roomNumber and Date(r.createdDate) = :today")
    int countCurrentCapacity(@Param("roomNumber") String roomNumber,@Param("today") java.sql.Date today);

  /**
   * 오늘 날짜 기준으로, 특정 강의실에 따라, permission에 따라 Reservation을 반환 하는데 가장 늦게 끝나는 예약 순으로 정렬 후 반환한다.
   */
    @Query("select r from Reservation r join fetch r.lab l join fetch r.member m where r.lab = :lab and Date(r.createdDate) = :today and r.permission = :permission order by r.endTime desc")
    Optional<List<Reservation>> findReservationWithPermissionByLabId(@Param("lab") Lab lab,@Param("today") java.sql.Date today,@Param("permission") boolean permission);

  /**
   * 오늘 특정 사용자가 예약 한 모든 내역을 반환한다.
   */
    @Query("select r from Reservation r join fetch r.lab l where r.member = :member and Date(r.createdDate) = :today order by r.startTime asc")
    Optional<List<Reservation>> findAllByMember(@Param("member") Member member , @Param("today") java.sql.Date today);

    /**
     * 오늘 예약한 목록 중에서 permission이 true 혹은 false에 따른 예약 내역 전체를 반환한다.
     */
    @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where Date(r.createdDate) = :today and r.permission = :permission order by r.startTime asc")
    Optional<List<Reservation>> findReservationsByDateAndPermission(@Param("today") java.sql.Date today, @Param("permission") boolean permission);
}
