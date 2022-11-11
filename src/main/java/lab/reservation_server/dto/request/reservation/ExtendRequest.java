package lab.reservation_server.dto.request.reservation;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예약 연장할때는 사용자 id와 연장할 예약의 id를 받는다.
 */
@Getter
@NoArgsConstructor
public class ExtendRequest {

    @NotNull(message = "연장할 예약의 고유한 id를 반드시 입력해주세요")
    @ApiModelProperty(value = "연장할 예약의 고유한 숫자 id")
    private Long reservationId;

}
