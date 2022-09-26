package lab.reservation_server.service;

import java.time.LocalDate;
import lab.reservation_server.dto.request.ExpireDate;
import lab.reservation_server.dto.request.TokenCheckDto;
import lab.reservation_server.dto.response.token.MemberIsAuth;
import lab.reservation_server.dto.response.token.TokenValue;

public interface TokenService {

    // create token with uuid
    String createToken(ExpireDate expirationDate);

    MemberIsAuth checkToken(TokenCheckDto tokenValue);
}
