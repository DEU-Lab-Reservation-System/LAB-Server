package lab.reservation_server.dto.request.reservation;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 미승인된 예약 중에서 승인 혹은 거절의 요청을 받는 dto
 */
@Getter
@NoArgsConstructor
public class PermissionUpdate {

  @NotEmpty(message = "승인 혹은 거절할 예약의 고유한 id 목록을 반드시 입력해주세요")
  @ApiModelProperty(value = "미승인된 예약 내역을 승인 혹은 거절할 예약의 고유한 id 목록")
  private List<Long> reservationIds;

  @ApiModelProperty(value = "승인 혹은 거절 여부 ex)true, false")
  private boolean state;

  @NotBlank(message = "실습실 방번호를 반드시 입력해주세요")
  @ApiModelProperty(value = "실습실 방번호")
  private String roomNum;

  public boolean getState() {
    return state;
  }

}
