package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lab.reservation_server.dto.request.MemberLogin;
import lab.reservation_server.dto.request.MemberSignUp;
import lab.reservation_server.dto.request.UserIdCheck;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.member.MemberInfo;
import lab.reservation_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Member Controller : 회원, 조교...")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member")
    @ApiOperation(value="회원가입" , notes = "학생 회원가입을 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> signUp(@RequestBody @Valid MemberSignUp memberSignUp) {
        if (memberService.signUp(memberSignUp)){
            return ResponseEntity.ok(new DefaultMessageResponse("회원가입 성공"));
        }
        return ResponseEntity.badRequest().body(new DefaultMessageResponse("회원가입 실패"));
    }

    @PostMapping("/api/member/login")
    @ApiOperation(value="로그인" , notes = "학생 로그인을 할 수 있다.")
    public ResponseEntity<MemberInfo> login(@RequestBody @Valid MemberLogin memberLogin) {
        MemberInfo memberInfo = memberService.login(memberLogin);
        return ResponseEntity.ok(memberInfo);
    }


    /**
     * 아이디 중복 확인
     */
    @PostMapping("/api/member/check")
    @ApiOperation(value="아이디 중복 확인" , notes = "아이디 중복 확인을 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> checkId(@RequestBody @Valid UserIdCheck userIdCheck) {
        if (!memberService.checkId(userIdCheck)){
            return ResponseEntity.ok(new DefaultMessageResponse("사용 가능한 아이디 입니다."));
        }
        return ResponseEntity.badRequest().body(new DefaultMessageResponse("이미 사용중인 아이디 입니다."));
    }
}
