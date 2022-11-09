package lab.reservation_server.dto.request.reservation;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특정 강의실, 특정 시간에 이용중인 혹은 이용 예정인 사람들의 정보를 요청하기 위한 DTO
 */
@Getter
@NoArgsConstructor
public class RoomAndTime {

    @NotBlank(message = "실습실 이용자 정보를 확인하기 위한 강의실 번호를 반드시 입력해주세요")
    @ApiModelProperty(value = "강의실 번호")
    private String roomNum;

    @NotBlank(message = "실습실 이용자 정보를 확인하기 위한 시작 시간을 반드시 입력해주세요")
    @ApiModelProperty(value = "시작 시간 ex) 09:00")
    private String startTime;

    @NotBlank(message = "실습실 이용자 정보를 확인하기 위한 종료 시간을 반드시 입력해주세요")
    @ApiModelProperty(value = "종료 시간 ex) 11:00")
    private String endTime;


    public LocalTime getStartLocalTime() {
        return LocalTime.parse(startTime);
    }

    public LocalTime getEndLocalTime() {
        return LocalTime.parse(endTime);
    }

    public LocalDateTime getStartLocalDateTime() {
        return LocalDateTime.of(LocalDate.now(), getStartLocalTime());
    }

    public LocalDateTime getEndLocalDateTime() {
        return LocalDateTime.of(LocalDate.now(), getEndLocalTime());
    }


}
