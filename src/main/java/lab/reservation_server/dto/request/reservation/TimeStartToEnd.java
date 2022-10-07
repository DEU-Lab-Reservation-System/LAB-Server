package lab.reservation_server.dto.request.reservation;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특정 강의실, 특정 시간에 예약이 가능한지 확인하기 위한 요청
 */
@Getter
@NoArgsConstructor
public class TimeStartToEnd {

    @NotBlank(message = "시작 시간을 입력해주세요.")
    @ApiModelProperty(value = "시작 시간", notes = "예약 현황을 확인하고자 하는 시작 시간을 입력해주세요.")
    private String startTime;

    @NotBlank(message = "종료 시간을 입력해주세요.")
    @ApiModelProperty(value = "종료 시간", notes = "예약 현황을 확인하고자 하는 종료 시간을 입력해주세요.")
    private String endTime;


    /**
     * String To LocalTime
     */
    public LocalTime getStartTime() {
        return LocalTime.parse(startTime);
    }

    /**
     * String To LocalTime
     */
    public LocalTime getEndTime() {
        return LocalTime.parse(endTime);
    }
}
