package lab.reservation_server.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.enums.Role;
import lab.reservation_server.dto.request.member.MemberLogin;
import lab.reservation_server.dto.request.member.MemberSignUp;
import lab.reservation_server.dto.request.member.MemberUpdate;
import lab.reservation_server.dto.request.member.UserIdCheck;
import lab.reservation_server.dto.response.member.MemberInfo;
import lab.reservation_server.dto.response.member.MemberSimpleInfo;
import lab.reservation_server.dto.response.member.MemberSimpleInfos;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.exception.DuplicateException;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.service.MemberService;
import lab.reservation_server.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ReservationService reservationService;

    /**
     * 회원가입
     * @param memberSignUp 회원가입 정보
     */
    @Override
    @Transactional
    public Boolean signUp(MemberSignUp memberSignUp) {
        // member 저장 후 unique 제약 조건으로 이미 존재하는 회원일 경우 error 처리
        try {
           memberRepository.save(memberSignUp.toEntity(memberSignUp));
        } catch (DataIntegrityViolationException e) {
          throw new DuplicateException("이미 존재하는 회원입니다.");
        }
      return true;
    }

    /**
     * 학생 로그인
     * @param memberLogin 로그인을 하기 위한 아이디 및 비밀번호 받는 dto
     * @return 학생의 최소한의 정보만 담은 MemberInfo Dto 반환
     */
    @Override
    @Transactional
    public MemberInfo login(MemberLogin memberLogin) {
      // check user id is valid
      Member memberFromDb = memberRepository.findByUserId(memberLogin.getUserId())
          .orElseThrow(() -> new BadRequestException("아이디가 유효하지 않습니다."));

      // check user password is valid
      if (!memberFromDb.getPassword().equals(memberLogin.getPassword())) {
        throw new BadRequestException("아이디 혹은 비밀번호가 유효하지 않습니다.");
      }

      // 사용자의 디바이스 토큰 업데이트
      memberFromDb.updateDeviceToken(memberLogin.getDeviceToken());

      // reservation service를 통해서 reservationInfo 가져오기
      ReservationInfo reservation =
          reservationService.getCurrentReservationFromMemberId(memberFromDb.getId());


      // return memberinfo
      return new MemberInfo(memberFromDb,reservation);
    }

    /**
     * 사용자의 아이디 중복 확인
     */
    @Override
    public boolean checkId(UserIdCheck userIdCheck) {
        return memberRepository.findByUserId(userIdCheck.getUserId()).isPresent();
    }

    /**
     * 회원 사용자 정보 업데이트
     */
    @Override
    @Transactional
    public MemberUpdate updateMember(MemberUpdate memberUpdate) {

      checkValidation(memberUpdate);

      Member member = memberRepository.findById(memberUpdate.getId())
          .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

      member.updateMemberInfo(memberUpdate);

      return new MemberUpdate(member);
    }

    @Override
    @Transactional
    public String deleteMember(String userId) {
        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        memberRepository.delete(member);
      return "탈퇴 성공";
    }

    @Override
    public MemberSimpleInfos getMemberList() {
        List<Member> members = memberRepository.findAllWithRole(Role.USER);

        List<MemberUpdate> memberUpdates =
          members.stream().map(MemberUpdate::new).collect(Collectors.toList());

      return new MemberSimpleInfos(memberUpdates);
    }

    @Override
    @Transactional
    public String warning(String userId) {

        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

        member.warning();

        return "경고 횟수 1회 증가";
    }

    @Override
    @Transactional
    public String resetWarning(String userId) {
        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

        member.resetWarning();

        return "경고 횟수 초기화";
    }

    /**
     * member의 인증여부 필드를 false로 모두 초기화
     */
    @Override
    @Transactional
    public void updateMemberIsAuthFalse() {

        memberRepository.updateMemberIsAuthFalse();
    }

  private void checkValidation(MemberUpdate memberUpdate) {

      memberRepository.findByUserId(memberUpdate.getUserId()).ifPresent(member -> {
        if (!member.getId().equals(memberUpdate.getId())) {
          throw new DuplicateException("이미 존재하는 아이디입니다.");
        }
      });

      memberRepository.findByPhoneNum(memberUpdate.getPhoneNum()).ifPresent(member -> {
        if (!member.getId().equals(memberUpdate.getId())) {
          throw new DuplicateException("이미 존재하는 전화번호 입니다.");
        }
      });
    }

}
