package lab.reservation_server.dto.request.reservation;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실습실 예약 요청
 */
@Getter
@NoArgsConstructor
public class BookRequest {

    /**
     * 예약자의 학번
     */
    @NotBlank(message = "예약자의 학번을 반드시 입력해주세요")
    @ApiModelProperty(value = "예약자의 학번(아이디)")
    private String userId;

    /**
     * 예약하고자 하는 강의실
     */
    @NotNull(message = "예약하고자 하는 강의실 번호를 반드시 입력해주세요")
    @ApiModelProperty(value = "예약하고자 하는 강의실 번호 ex)911")
    private String roomNum;

    /**
     * 예약하고자 하는 시작 시간
     */
    @NotBlank(message = "예약하고자 하는 시작 시간을 반드시 입력해주세요")
    @ApiModelProperty(value = "예약하고자 하는 시작 시간 ex) 09:00")
    private String startTime;

    /**
     * 예약하고자 하는 종료 시간
     */
    @NotBlank(message = "예약하고자 하는 종료 시간을 반드시 입력해주세요")
    @ApiModelProperty(value = "예약하고자 하는 종료 시간 ex) 15:00")
    private String endTime;

    /**
    * 예약 하고자 하는 팀 인원
    */
    @NotNull(message = "예약 하고자 하는 팀 인원을 반드시 입력해주세요")
    @ApiModelProperty(value = "예약 하고자 하는 팀 인원")
    private Integer teamSize;

    /**
     * 예약한 좌석의 번호
     */
    @NotNull(message = "예약한 좌석의 번호를 반드시 입력해주세요")
    @ApiModelProperty(value = "예약한 좌석의 번호")
    private String seatNum;


    public LocalDateTime getStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.parse(this.startTime));
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.parse(this.endTime));
    }

    public LocalDateTime getExtensionTime() {
        // get extension time before 30 min from end time
        return LocalDateTime.of(LocalDate.now(), LocalTime.parse(this.endTime).minusMinutes(30));
    }


    public Reservation toApprovedReservation(Member member, Lab lab) {
        return Reservation.builder()
            .member(member)
            .lab(lab)
            .startTime(getStartTime())
            .endTime(getEndTime())
            .extensionTime(getExtensionTime())
            .seatNum(seatNum)
            .permission(true)
            .build();
    }

    public Reservation toUnapprovedReservation(Member member, Lab lab) {
        return Reservation.builder()
            .member(member)
            .lab(lab)
            .startTime(getStartTime())
            .endTime(getEndTime())
            .extensionTime(getExtensionTime())
            .seatNum(seatNum)
            .permission(false)
            .build();
    }
}
