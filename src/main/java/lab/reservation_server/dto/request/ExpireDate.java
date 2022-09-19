package lab.reservation_server.dto.request;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpireDate {

  @NotNull(message = "만료일을 반드시 입력해주세요")
  @ApiModelProperty(value = "토큰 유효 기간 , yy--mm--dd 형식으로 입력해주세요")
  private LocalDate expireDate;
}
