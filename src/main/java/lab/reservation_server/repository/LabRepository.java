package lab.reservation_server.repository;

import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.dto.response.lab.LectureInfoForLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabRepository extends JpaRepository<Lab, Long> {

    /**
     * 강의실 번호를 통해서 , 해당 강의실에 열린 강좌 데이터를 dto로 반환
     */
    @Query("select new lab.reservation_server.dto.response.lab.LectureInfoForLab" +
        "(l.id, l.title, l.professor,l.code , l.day, l.startTime, l.endTime, l.startDate, l.endDate) " +
        "from Lecture l join l.lab lb where lb.roomNumber = :roomNum")
    Optional<List<LectureInfoForLab>> findLecturesByLabRoomNumber(@Param("roomNum") String roomNumber);

    Optional<Lab> findByRoomNumber(String roomNumber);
}
