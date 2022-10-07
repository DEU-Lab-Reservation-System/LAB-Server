package lab.reservation_server.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

  // check duplicate lecture
  // @Query("select l from Lecture l where l.lab.roomNumber = :roomNum and l.day = :day and l.endTime > :startTime and l.startTime < :endTime")
  @Query("select l from Lecture l join fetch l.lab lb where lb.roomNumber = :roomNum and l.day = :day and l.endTime > :startTime and l.startTime < :endTime")
  Optional<Lecture> checkDuplicate(@Param("roomNum") String roomNumber,@Param("day") String day,@Param("startTime") LocalTime startTime,@Param("endTime") LocalTime endTime);

  Optional<Lecture> findByCode(String code);

  @Modifying
  @Query("delete from Lecture l where l.code = :code")
  void deleteAllByCode(@Param("code") String code);

  boolean existsByCode(String code);


  /**
   * 현재 시간에 강의가 있는지 확인 (지난 학기 개설 과목을 DB에서 삭제하지 않아도 올바른 데이터 반환)
   */
  @Query("select l from Lecture l where l.lab =:lab and l.day = :day and l.endTime >= :now and l.startTime <= :now and l.startDate <= :today and l.endDate >= :today")
  Optional<Lecture> checkNowByLabId(@Param("lab") Lab lab, @Param("day") String day, @Param("now") LocalTime now, @Param("today") LocalDate today);

  /**
   * 특정 강의실, 특정 시간대, 오늘 요일에 강의가 있는지 확인 (지난 학기 개설 과목을 DB에서 삭제하지 않아도 올바른 데이터 반환)
   */
  @Query("select l from Lecture l where l.lab =:lab and l.day = :day and l.endTime >= :startTime and l.startTime <= :endTime and l.startDate <= :today and l.endDate >= :today")
  Optional<Lecture> checkNowByLabIdBetweenTime(@Param("lab") Lab lab,@Param("day") String day,@Param("startTime") LocalTime startTime,@Param("endTime") LocalTime endTime,@Param("today") LocalDate today);
}
