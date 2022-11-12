package lab.reservation_server.dto.response.member;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberUpdate {

  private Long id;

  private String userId;

  private String password;
  private String name;
  private String email;

  private String major;

  private String phoneNum;
  private String role;

  private String deviceToken;

  private Boolean isAuth;

}
