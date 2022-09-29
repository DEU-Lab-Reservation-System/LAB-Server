package lab.reservation_server.dto.response.lab;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 강의 정보
 */
@Getter
@AllArgsConstructor
public class LectureInfoForLab {

    private Long id;
    private String title;
    private String professor;
    private String code;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;


}
