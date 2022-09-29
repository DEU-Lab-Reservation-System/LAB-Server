package lab.reservation_server.repository;

import java.time.LocalTime;
import java.util.Optional;
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
}
