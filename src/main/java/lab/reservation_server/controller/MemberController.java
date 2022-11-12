package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lab.reservation_server.dto.request.member.MemberLogin;
import lab.reservation_server.dto.request.member.MemberSignUp;
import lab.reservation_server.dto.request.member.MemberUpdate;
import lab.reservation_server.dto.request.member.UserIdCheck;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.member.MemberInfo;
import lab.reservation_server.dto.response.member.MemberSimpleInfos;
import lab.reservation_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Member Controller : 회원, 조교...")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/api/member")
    @ApiOperation(value="회원가입" , notes = "학생 회원가입을 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> signUp(@RequestBody @Valid MemberSignUp memberSignUp) {
        if (memberService.signUp(memberSignUp)){
            return ResponseEntity.ok(new DefaultMessageResponse("회원가입 성공"));
        }
        return ResponseEntity.badRequest().body(new DefaultMessageResponse("회원가입 실패"));
    }

    /**
     * 로그인
     */
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


    /**
     * 회원 정보 수정 (학생)
     */
     @PutMapping("/api/member")
     @ApiOperation(value="회원(학생) 정보 수정" , notes = "회원(학생) 정보 수정을 할 수 있다.")
     public ResponseEntity<MemberUpdate> updateMember(@RequestBody @Valid MemberUpdate memberUpdate) {

         MemberUpdate member = memberService.updateMember(memberUpdate);

         return ResponseEntity.ok(member);
     }

    /**
     * 회원정보 탈퇴
     */
    @DeleteMapping("/api/member/{userId}")
    @ApiImplicitParam(name = "userId" , value = "사용자 아이디(학번)" , required = true)
    @ApiOperation(value="회원정보 탈퇴" , notes = "회원정보 탈퇴를 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> deleteMember(@PathVariable String userId) {
        String message = memberService.deleteMember(userId);
        return ResponseEntity.ok(new DefaultMessageResponse(message));
    }


    /**
     * 조교는 경고를 부여하기 위해 재학생 목록을 조회할 수 있다.
     */
    @GetMapping("/api/member")
    @ApiOperation(value="조교는 재학생 목록을 조회할 수 있다." , notes = "조교는 재학생 목록을 조회할 수 있다.")
    public ResponseEntity<MemberSimpleInfos> getMemberList() {
        MemberSimpleInfos memberSimpleInfos = memberService.getMemberList();
        return ResponseEntity.ok(memberSimpleInfos);
    }

    /**
     * 경고 부여
     */
    @PutMapping("/api/member/{userId}/warning")
    @ApiImplicitParam(name = "userId" , value = "사용자 아이디(학번)" , required = true)
    @ApiOperation(value="경고 부여" , notes = "경고 부여를 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> warning(@PathVariable String userId) {
        String message = memberService.warning(userId);
        return ResponseEntity.ok(new DefaultMessageResponse(message));
    }

    /**
     * 경고 초기화
     */
    @PutMapping("/api/member/{userId}/warning/reset")
    @ApiImplicitParam(name = "userId" , value = "사용자 아이디(학번)" , required = true)
    @ApiOperation(value="경고 초기화" , notes = "경고 초기화를 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> resetWarning(@PathVariable String userId) {
        String message = memberService.resetWarning(userId);
        return ResponseEntity.ok(new DefaultMessageResponse(message));
    }


}
