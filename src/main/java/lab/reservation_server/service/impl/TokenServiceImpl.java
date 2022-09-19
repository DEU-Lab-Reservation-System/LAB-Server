package lab.reservation_server.service.impl;

import java.time.LocalDate;
import lab.reservation_server.domain.Token;
import lab.reservation_server.dto.request.ExpireDate;
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
}
