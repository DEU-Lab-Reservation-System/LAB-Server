package lab.reservation_server.dto.response.member;

import lab.reservation_server.domain.Member;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 학생의 민감한 정보는 제외하고 기본적인 정보만 반환
 */
@Getter
@AllArgsConstructor
public class MemberInfo {

    private Long id;

    private ReservationInfo reservation;

    private String userId;

    private String password;
    private String name;
    private String email;

    private String phoneNum;
    private String role;

    private String deviceToken;

    private Boolean isAuth;

    public MemberInfo(Member saved, ReservationInfo reservationInfo) {
      this.id = saved.getId();
      this.reservation = reservationInfo;
      this.userId = saved.getUserId();
      this.password = saved.getPassword();
      this.name = saved.getName();
      this.email = saved.getEmail();
      this.phoneNum = saved.getPhoneNum();
      this.role = saved.getRole().getAuthority();
      this.deviceToken = saved.getDeviceToken();
      this.isAuth = saved.getIsAuth();
    }
}
