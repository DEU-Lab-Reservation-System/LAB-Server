package lab.reservation_server.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 하고자 하는 사용자의 id와 한 학기에 유효한 토큰을 입력한다.
 */
@Getter
@NoArgsConstructor
public class TokenCheckDto {

    @NotBlank(message = "검증된 사용자로 인정 받기 위한 사용자의 학번을 넣어주세요")
    @ApiModelProperty(value = "로그인한 사용자의 학번, 검증된 학생인지 확인하기 위해")
    private String userId;

    @NotBlank(message = "검증된 사용자로 인정 받기 위한 사용자의 토큰을 넣어주세요")
    @ApiModelProperty(value = "한 학기에 제공된 토큰, 검증된 학생인지 확인하기 위해")
    private String token;
}
