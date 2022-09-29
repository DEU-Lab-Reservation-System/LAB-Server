package lab.reservation_server.dto.response.lecture;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureInfo {
    private Long id;
    private String title;
    private String professor;
    private String code;
    private String roomNum;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
}
