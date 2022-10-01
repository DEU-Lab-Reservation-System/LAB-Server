package lab.reservation_server.service.impl;

import lab.reservation_server.domain.Member;
import lab.reservation_server.dto.request.MemberLogin;
import lab.reservation_server.dto.request.MemberSignUp;
import lab.reservation_server.dto.response.member.MemberInfo;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.exception.DuplicateException;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.repository.TokenRepository;
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
          reservationService.getReservationFromMemberId(memberFromDb.getId());


      // return memberinfo
      return new MemberInfo(memberFromDb,reservation);
    }
}
