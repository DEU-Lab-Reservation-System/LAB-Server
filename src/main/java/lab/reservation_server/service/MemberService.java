package lab.reservation_server.service;

import lab.reservation_server.dto.request.member.MemberLogin;
import lab.reservation_server.dto.request.member.MemberSignUp;
import lab.reservation_server.dto.request.member.UserIdCheck;
import lab.reservation_server.dto.response.member.MemberInfo;

public interface MemberService {

    Boolean signUp(MemberSignUp memberSignUp);

    MemberInfo login(MemberLogin memberLogin);

    boolean checkId(UserIdCheck userIdCheck);
}
