package lab.reservation_server.dto.response.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자가 토큰 인증에 대한 성공 여부를 true 혹은 false로 반환한다.
 */
@Getter
@AllArgsConstructor
public class MemberIsAuth {

   boolean auth;

}
