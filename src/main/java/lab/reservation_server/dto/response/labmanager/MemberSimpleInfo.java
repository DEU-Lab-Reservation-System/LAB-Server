package lab.reservation_server.dto.response.labmanager;

import lab.reservation_server.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 오늘 날짜에 해당 강의실을 담당하고 있는 방장의 정보를 반환
 */
@Getter
@AllArgsConstructor
public class MemberSimpleInfo {

  private Long id;

  // 학생의 아이디 혹은 학번으로 들어간다.
  private String userId;

  private String name;

  private String phoneNum;

  public static MemberSimpleInfo toMemberSimpleInfo(Member member) {
    return new MemberSimpleInfo(member.getId(), member.getUserId(), member.getName(), member.getPhoneNum());
  }

}
