package lab.reservation_server.service;

import java.time.LocalDate;
import lab.reservation_server.dto.request.ExpireDate;

public interface TokenService {

    // create token with uuid
    String createToken(ExpireDate expirationDate);
}
