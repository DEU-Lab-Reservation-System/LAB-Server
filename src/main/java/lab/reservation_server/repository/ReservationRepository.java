package lab.reservation_server.repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * 오늘 날짜 기준, Member가 가장 최근에 예약한 이상적인 Reservation을 가져온다.
     * 부등호 check 완료
     */
    @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId and r.endTime > :now order by r.startTime asc")
    Optional<List<Reservation>> findReservationByMemberId(@Param("memberId") Long memberId,
                                                          @Param("now") LocalDateTime now);

    @Query("select r from Reservation r where r.member =:member and r.lab =:lab and Date(r.createdDate) = :today order by r.endTime desc")
    List<Reservation> findReservationByMemberAndLab(@Param("member") Member member,@Param("lab") Lab lab,@Param("today") Date today);

    /**
     * 특정 사용자 예약 목록 중에서 ture, false 예약 내역 중에서 제일 최근내역을 가져온다.
     */
    @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where m.id = :memberId and r.permission = :permission and Date(r.createdDate) = :today order by r.endTime desc")
    Optional<List<Reservation>> findApprovedReservationByMemberId(@Param("memberId") Long memberId,
                                                                  @Param("permission") Boolean permission,
                                                                  @Param("today") Date today);



    /**
     * 해당 강의실에 현재 시간에 이용중인 예약 내역을 반환한다.
     * 사용자 화면 기준으로 사용되는 쿼리, 왜냐하면 사용자 입장에서는 오후반 신청할때 아직 미승인 예약에 대해서도 알아야지 해당 좌석을 피해서
     * 예약을 진행해야 하기 떄문이다.
     * 부등호 check 완료, 변경 완료
     */
    @Query("select r from Reservation r where r.lab =:lab and r.endTime >= :now and r.startTime <= :now")
    Optional<List<Reservation>> findCurrentReservation(@Param("lab") Lab lab,@Param("now") LocalDateTime now);

    /**
     * 조교 화면 기준으로 사용되는 쿼리, 조교 입장에서 특정 강의실,
     * 특정 시간대에 조회하는 경우는 permission이 true인 경우의 승인된 예약 내역을 조회한다.
     * 부등호 check 완료
     */
    @Query("select r from Reservation r where r.lab =:lab and r.startTime >= :startTime and r.endTime <= :endTime and r.permission = :permission order by r.startTime asc")
    Optional<List<Reservation>> findCurrentReservationWithPermission(@Param("lab") Lab lab,
                                                                     @Param("startTime") LocalDateTime startTime,
                                                                     @Param("endTime") LocalDateTime endTime,
                                                                     @Param("permission") boolean permission);

    /**
     * 특정 강의실, 특정 시간대 범위에 이용중인 reservation을 반환한다. (오늘 기준으로 검색, 스케줄러를 통해 일주일 단위로 삭제 해도
     * 올바른 데이터 반환)
     * 부등호 check 완료
     */
    @Query("select r from Reservation r where r.lab =:lab and r.endTime > :startTime and r.startTime < :endTime and Date(r.createdDate) = :today")
    Optional<List<Reservation>> findCurrentReservationBetweenTime(@Param("lab") Lab lab,
                                                                  @Param("startTime") LocalDateTime startTime,
                                                                  @Param("endTime") LocalDateTime endTime,
                                                                  @Param("today") java.sql.Date today);



    /**
     * 특정 시간대에 이용중인 예약 내역의 개수를 반환한다.
     * 부등호 check 완료
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
    * 사용자가 해당 서비스를 이용하면서 예약했던 모든 내역을 조회한다.
   */
    @Query("select r from Reservation r join fetch r.lab l where r.member = :member order by r.startTime asc")
    Optional<List<Reservation>> findAllLastReservationByMember(@Param("member") Member member);

    /**
     * 오늘 예약한 목록 중에서 permission이 true 혹은 false에 따른 예약 내역 전체를 반환한다.
     */
    @Query("select r from Reservation r join fetch r.member m join fetch r.lab l where Date(r.createdDate) = :today and r.permission = :permission order by r.startTime asc")
    Optional<List<Reservation>> findReservationsByDateAndPermission(@Param("today") java.sql.Date today, @Param("permission") boolean permission);

    /**
     * 특정 강의실의 오늘 예약 내역 중에서 미승인된 permission의 상태를 update 해준다.
     */
    @Modifying
    @Query("update Reservation r set r.permission = :permission where r.id in :ids")
    void updatePermission(@Param("ids") List<Long> reservationIds, @Param("permission") boolean permission);

    /**
     * 특정 강의실, 오늘 예약, reservationIds에서 가장 오랫동안 사용하는 내역을 조회한다.
     */
    @Query("select r from Reservation r join fetch r.member m where r.lab = :lab and r.id in :ids order by r.endTime desc")
    Optional<List<Reservation>> findMemberWithLongestTime(@Param("lab") Lab lab, @Param("ids") List<Long> reservationIds);

    @Modifying
    @Query("delete from Reservation r where r.id in :ids")
    void deleteByIds(@Param("ids") List<Long> reservationIds);
}
