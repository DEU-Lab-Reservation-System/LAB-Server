package lab.reservation_server.service;

import lab.reservation_server.domain.Lab;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;

public interface LabManagerService {


    /**
     * 현재 시간과 lab id를 통해서 방장인 Member 반환
     */
    MemberSimpleInfo searchMemberByLabId(Long labId);

    void updateLabManager(Lab lab, BookRequest book);
}
