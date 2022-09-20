package lab.reservation_server.service.impl;

import lab.reservation_server.dto.request.MemberSignUp;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.exception.DuplicateException;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.repository.TokenRepository;
import lab.reservation_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    /**
     * 회원가입
     * @param memberSignUp 회원가입 정보
     */
    @Override
    @Transactional
      public Boolean SignUp(MemberSignUp memberSignUp) {
        // check token is valid
        tokenRepository.findByValue(memberSignUp.getToken())
          .orElseThrow(() -> new BadRequestException("토큰이 유효하지 않습니다."));

        // member 저장
        try {
           memberRepository.save(memberSignUp.toEntity(memberSignUp));
        } catch (DataIntegrityViolationException e) {
          throw new DuplicateException("이미 존재하는 회원입니다.");
        }

      return true;
    }
}
