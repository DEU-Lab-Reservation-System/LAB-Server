package lab.reservation_server.dto.request;



import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 학생이 회원가입시 제공하는 데이터를 바인딩 하기 위한 Request DTO
 * @author soochan lee
 */
@Getter
@NoArgsConstructor
public class MemberSignUp {

    @NotBlank(message = "학번을 넣어주세요, 공백 X")
    @ApiModelProperty(value = "학생의 학번")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$", message = "비밀번호는 영문자와 숫자를 포함한 6자리 이상이어야 합니다.")
    @ApiModelProperty(value = "학생의 비밀번호 , 영*숫자 포함 6자리 이상")
    private String password;

    @NotBlank(message = "이름을 입력해주세요, 공백 X")
    @ApiModelProperty(value = "학생의 이름")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요, 공백 X")
    @Email(message = "이메일 형식이 아닙니다.")
    @ApiModelProperty(value = "학생의 이메일")
    private String email;

    @NotNull(message = "폰번호를 입력해주세요, 공백 X")
    @ApiModelProperty(value = "학생의 폰번호 , '-' 없이 입력해주세요")
    private String phoneNum;


    public Member toEntity(MemberSignUp memberSignUp) {
        return Member.builder()
                .userId(memberSignUp.getUserId())
                .password(memberSignUp.getPassword())
                .name(memberSignUp.getName())
                .email(memberSignUp.getEmail())
                .phoneNum(memberSignUp.getPhoneNum())
                .role(Role.USER)
                .isAuth(false)
                .build();
    }
}
