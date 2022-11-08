package lab.reservation_server.dto.request.member;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserId {

  // 6자리 이상
  @NotBlank(message = "아이디는 필수 입력 값입니다.")
  @ApiModelProperty(value = "모든 예약 내역을 조회하고자 하는 사용자의 아이디")
  private String userId;
}
