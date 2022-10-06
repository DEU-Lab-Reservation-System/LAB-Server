package lab.reservation_server.service;

import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;

public interface LabManagerService {


    /**
     * 현재 시간과 lab id를 통해서 방장인 Member 반환
     */
    MemberSimpleInfo searchMemberByLabId(Long labId);

}
