package lab.reservation_server.service;

import lab.reservation_server.dto.request.token.ExpireDate;
import lab.reservation_server.dto.request.member.TokenCheckDto;
import lab.reservation_server.dto.response.token.MemberIsAuth;

public interface TokenService {

    // create token with uuid
    String createToken(ExpireDate expirationDate);

    MemberIsAuth checkToken(TokenCheckDto tokenValue);
}
