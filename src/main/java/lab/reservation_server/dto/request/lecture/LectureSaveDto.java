package lab.reservation_server.dto.request.lecture;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Lecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 강의 시간표 추가 dto
 */
@Getter
@NoArgsConstructor
public class LectureSaveDto {

    @NotBlank(message = "강의명은 필수 입력 값입니다.")
    @ApiModelProperty(value = "수업명")
    private String title;

    @NotBlank(message = "강의 담당교수님 성함은 필수 입니다.")
    @ApiModelProperty(value = "수업 담당 교수님 성함")
    private String professor;

    @NotBlank(message = "강의 코드는 필수 입니다.")
    @ApiModelProperty(value = "앱에서 생성된 수업 코드")
    private String code;


    @NotNull(message = "강의 시작 날짜는 필수 입니다.")
    @ApiModelProperty(value = "수업 시작 날짜 - 개강 날짜")
    private LocalDate startDate;


    /**
     * localDate에 한해서는 NotNull 적용
     */
    @NotNull(message = "강의 종료 날짜는 필수 입니다.")
    @ApiModelProperty(value = "수업 종료 날짜 - 종강 날짜")
    private LocalDate endDate;

    @NotBlank(message = "강의실 방번호는 필수 입니다.")
    @ApiModelProperty(value = "수업이 진행될 강의실 방번호")
    private String roomNumber;

    @NotBlank(message = "강의 시작 시간은 필수 입니다.")
    @ApiModelProperty(value = "수업 시작 시간, HH:MM")
    private String startTime;

    @NotBlank(message = "강의 요일은 필수 입니다.")
    @ApiModelProperty(value = "수업 요일, ex) 월요일...")
    private String day;

    @NotBlank(message = "강의 종료 시간은 필수 입니다.")
    @ApiModelProperty(value = "수업 종료 시간, HH:MM")
    private String endTime;

    /**
     * lectureSaveDto To Lecture Entity
     */
    public Lecture toEntity(LectureSaveDto lectureSaveDto, Lab lab) {
        return Lecture.builder()
                .title(title)
                .professor(professor)
                .code(code)
                .startDate(startDate)
                .endDate(endDate)
                .lab(lab)
                .startTime(lectureSaveDto.getStartTime())
                .endTime(lectureSaveDto.getEndTime())
                .day(day)
                .build();
    }

    public LocalTime getStartTime(){
        return LocalTime.parse(startTime);
    }

    public LocalTime getEndTime(){
        return LocalTime.parse(endTime);
    }

}
