package lab.reservation_server.service;

import lab.reservation_server.dto.request.MemberLogin;
import lab.reservation_server.dto.request.MemberSignUp;
import lab.reservation_server.dto.response.MemberInfo;

public interface MemberService {

    Boolean signUp(MemberSignUp memberSignUp);

    MemberInfo login(MemberLogin memberLogin);

}
