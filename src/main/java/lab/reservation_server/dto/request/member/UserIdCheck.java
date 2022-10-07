package lab.reservation_server.dto.request.member;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 아이디의 중복 확인을 위한 요청 객체
 */
@Getter
@NoArgsConstructor
public class UserIdCheck {

    // 6자리 이상
    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$", message = "아이디는 영문, 숫자 중 6자리로 구성되어야 합니다.")
    @ApiModelProperty(value = "중복 확인하고자 하는 아이디")
    private String userId;
}
