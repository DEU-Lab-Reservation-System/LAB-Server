package lab.reservation_server.service;

import lab.reservation_server.dto.request.member.MemberLogin;
import lab.reservation_server.dto.request.member.MemberSignUp;
import lab.reservation_server.dto.request.member.MemberUpdate;
import lab.reservation_server.dto.request.member.UserIdCheck;
import lab.reservation_server.dto.response.member.MemberInfo;
import lab.reservation_server.dto.response.member.MemberSimpleInfos;

public interface MemberService {

    Boolean signUp(MemberSignUp memberSignUp);

    MemberInfo login(MemberLogin memberLogin);

    boolean checkId(UserIdCheck userIdCheck);

    MemberUpdate updateMember(MemberUpdate memberUpdate);

    String deleteMember(String userId);

    MemberSimpleInfos getMemberList();

    String warning(String userId);

    String resetWarning(String userId);
}
