package lab.reservation_server.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureRepository extends JpaRepository<Lecture, Long> {


  /**
   * 강의실 번호, 요일, 시작 시간, 종료시간, 오늘 날짜 기준으로 유효한... 통해서 해당 강의실에 똑같은 시간대로 강의가 존재하는지 확인
   */
  @Query("select l from Lecture l join fetch l.lab lb where lb.roomNumber = :roomNum and l.day = :day and l.endTime > :startTime and l.startTime < :endTime and l.startDate <= :endDate and l.endDate >= :startDate")
  Optional<List<Lecture>> checkDuplicate(@Param("roomNum") String roomNumber, @Param("day") String day, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
                                         @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  /**
   * 과목 코드를 통해서 강의 삭제
   */
  @Modifying
  @Query("delete from Lecture l where l.code = :code")
  void deleteAllByCode(@Param("code") String code);

  boolean existsByCode(String code);


  /**
   * 현재 시간에 강의가 있는지 확인 (지난 학기 개설 과목을 DB에서 삭제하지 않아도 올바른 데이터 반환)
   * 부등호 check 완료
   */
  @Query("select l from Lecture l where l.lab =:lab and l.day = :day and l.endTime >= :now and l.startTime <= :now and l.startDate <= :today and l.endDate >= :today")
  Optional<Lecture> checkNowByLabId(@Param("lab") Lab lab, @Param("day") String day, @Param("now") LocalTime now, @Param("today") LocalDate today);

  /**
   * 특정 강의실, 특정 시간대, 오늘 요일에 강의가 있는지 확인 (지난 학기 개설 과목을 DB에서 삭제하지 않아도 올바른 데이터 반환
   * 부등호 check 완료, 변경 완료
   */
  @Query("select l from Lecture l where l.lab =:lab and l.day = :day and l.endTime > :startTime and l.startTime < :endTime and l.startDate <= :today and l.endDate >= :today")
  Optional<List<Lecture>> checkNowByLabIdBetweenTime(@Param("lab") Lab lab, @Param("day") String day, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, @Param("today") LocalDate today);

  @Query("select l from Lecture l join fetch l.lab lab where l.startDate <= :today and l.endDate >= :today")
  Optional<List<Lecture>> findAllWithDate(@Param("today") LocalDate today);

  @Query("select l from Lecture l where l.code = :code and l.endDate >= :today")
  Optional<Lecture> findByCodeWithDate(@Param("code") String code, @Param("today") LocalDate today);
}
