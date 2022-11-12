package lab.reservation_server.dto.response.member;

import java.util.List;
import lab.reservation_server.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSimpleInfo {

    private Long id;

    private String userId;

    private String name;

    private String major;

    private String role;


  public MemberSimpleInfo(Member member) {
    this.id = member.getId();
    this.userId = member.getUserId();
    this.name = member.getName();
    this.major = member.getMajor();
    this.role = member.getRole().getAuthority();
  }


}
