package lab.reservation_server.repository;

import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.dto.response.lab.LectureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabRepository extends JpaRepository<Lab, Long> {

    //https://discourse.hibernate.org/t/query-specified-join-fetching-but-the-owner-of-the-fetched-association-was-not-present-in-the-select-list/258
    @Query("select new lab.reservation_server.dto.response.lab.LectureInfo" +
        "(l.id, l.title, l.professor, l.day, l.startTime, l.endTime, l.startDate, l.endDate) " +
        "from Lecture l join l.lab lb where lb.roomNumber = :roomNum")
    Optional<List<LectureInfo>> findLecturesByLabRoomNumber(@Param("roomNum") String roomNumber);

    Optional<Lab> findByRoomNumber(String roomNumber);
}
