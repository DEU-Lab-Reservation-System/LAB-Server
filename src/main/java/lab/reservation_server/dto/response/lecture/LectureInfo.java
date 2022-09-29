package lab.reservation_server.dto.response.lecture;


import lab.reservation_server.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureInfo {
    private Long id;
    private String title;
    private String professor;
    private String code;
    private String roomNumber;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;

    public LectureInfo(Lecture lecture, String roomNum) {
        this.id = lecture.getId();
        this.title = lecture.getTitle();
        this.professor = lecture.getProfessor();
        this.code = lecture.getCode();
        this.roomNumber = roomNum;
        this.dayOfWeek = lecture.getDay();
        this.startTime = lecture.getStartTime().toString();
        this.endTime = lecture.getEndTime().toString();
        this.startDate = lecture.getStartDate().toString();
        this.endDate = lecture.getEndDate().toString();
    }
}
