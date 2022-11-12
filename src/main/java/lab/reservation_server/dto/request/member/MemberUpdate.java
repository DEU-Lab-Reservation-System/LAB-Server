package lab.reservation_server.dto.request.member;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lab.reservation_server.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정할때 사용하는 request
 */
@Getter
@NoArgsConstructor
public class MemberUpdate {

    @NotNull(message = "사용자의 고유한 숫자 id를 입력해주세요.")
    @ApiModelProperty(value = "사용자의 고유한 숫자 id")
    private Long id;

    @NotBlank(message = "디바이스 토큰을 입력해주세요.")
    @ApiModelProperty(value = "디바이스 토큰")
    private String deviceToken;

    @NotBlank(message = "사용자의 이메일을 입력해주세요")
    @ApiModelProperty(value = "사용자의 이메일")
    private String email;


    @ApiModelProperty(value = "사용자의 토큰 인증여부")
    private Boolean isAuth;

    @NotBlank(message = "사용자의 이름을 입력해주세요")
    @ApiModelProperty(value = "사용자의 이름")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$", message = "비밀번호는 영문자와 숫자를 포함한 6자리 이상이어야 합니다.")
    @ApiModelProperty(value = "학생의 비밀번호 , 영*숫자 포함 6자리 이상")
    private String password;

    @NotBlank(message = "사용자의 휴대번호를 입력해주세요")
    @ApiModelProperty(value = "사용자의 휴대번호")
    private String phoneNum;

    @NotBlank(message = "사용자의 권한을 입력해주세요")
    @ApiModelProperty(value = "사용자의 권한, ex) ADMIN, PROF, USER, USER_GRADUATE, USER_TAKEOFF")
    private String role;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$", message = "아이디는 영문 혹은 숫자로 6자리 이상으로 구성되어야 합니다.")
    @ApiModelProperty(value = "학생의 학번, 아이디는 영문 혹은 숫자로 6자리 이상으로 구성되어야 합니다.")
    private String userId;

    @NotBlank(message = "사용자의 학과를 입력해주세요")
    @ApiModelProperty(value = "사용자의 학과")
    private String major;

    public MemberUpdate (Member member){
        this.id = member.getId();
        this.deviceToken = member.getDeviceToken();
        this.email = member.getEmail();
        this.isAuth = member.getIsAuth();
        this.name = member.getName();
        this.password = member.getPassword();
        this.phoneNum = member.getPhoneNum();
        this.role = member.getRole().getAuthority();
        this.userId = member.getUserId();
        this.major = member.getMajor();
    }


}
