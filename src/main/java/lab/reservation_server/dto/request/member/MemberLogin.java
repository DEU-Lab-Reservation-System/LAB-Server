package lab.reservation_server.dto.request.member;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 학생이 로그인을 하기 위한 학번과 비밀번호 입력
 */
@Getter
@NoArgsConstructor
public class MemberLogin {

    @NotBlank(message = "학번을 넣어주세요, 공백 X")
    @ApiModelProperty(value = "학생의 학번")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @ApiModelProperty(value = "학생의 비밀번호")
    private String password;

    @NotBlank(message = "토큰은 필수 입력 값입니다.")
    @ApiModelProperty(value = "디바이스 토큰")
    private String deviceToken;

}
