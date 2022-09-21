package lab.reservation_server.dto.response;

import lab.reservation_server.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 학생의 민감한 정보는 제외하고 기본적인 정보만 반환
 */
@Getter
@AllArgsConstructor
public class MemberInfo {

    private Long id;
    private String userId;
    private String name;
    private String email;
    private String role;

    private Boolean isAuth;

    public MemberInfo(Member saved) {
      this.id = saved.getId();
      this.userId = saved.getUserId();
      this.name = saved.getName();
      this.email = saved.getEmail();
      this.role = saved.getRole().getAuthority();
      this.isAuth = saved.getIsAuth();
    }
}
