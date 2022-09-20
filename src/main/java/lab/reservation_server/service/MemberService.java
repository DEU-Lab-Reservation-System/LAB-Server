package lab.reservation_server.service;

import lab.reservation_server.dto.request.MemberSignUp;

public interface MemberService {

    Boolean SignUp(MemberSignUp memberSignUp);

}
