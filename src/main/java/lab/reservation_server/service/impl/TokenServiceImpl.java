package lab.reservation_server.service.impl;

import java.time.LocalDate;
import lab.reservation_server.domain.Token;
import lab.reservation_server.dto.request.ExpireDate;
import lab.reservation_server.dto.request.TokenCheckDto;
import lab.reservation_server.dto.response.token.MemberIsAuth;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.repository.TokenRepository;
import lab.reservation_server.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    /**
     * 조교로 부터 만료 날짜를 받는다.
     * 토큰을 UUID 형식으로 발급하여 DB에 저장
     * @return token
     */
    @Override
    @Transactional
    public String createToken(ExpireDate expirationDate) {
      Token token = new Token(expirationDate.getExpireDate());
      tokenRepository.save(token);
      return token.getValue();
    }

  @Override
  @Transactional
  public MemberIsAuth checkToken(TokenCheckDto tokenCheckDto) {

    Token token = tokenRepository.findByValue(tokenCheckDto.getToken())
                      .orElseThrow(() -> new BadRequestException("토큰이 존재하지 않습니다."));

    if(token.getExpiration().isBefore(LocalDate.now())) {
      throw new BadRequestException("토큰이 만료되었습니다.");
    }

    // 정상 처리일 경우 해당 사용자의 isAuth를 true로 변경
    memberRepository.findByUserId(tokenCheckDto.getUserId())
        .orElseThrow(() -> new BadRequestException("해당 사용자가 존재하지 않습니다."))
        .updateAuth(true);

    return new MemberIsAuth(true);
  }
}
