package lab.reservation_server.service.impl;

import java.time.LocalDate;
import lab.reservation_server.domain.Member;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lab.reservation_server.repository.LabManagerRepository;
import lab.reservation_server.service.LabManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabManagerImpl implements LabManagerService {

    private final LabManagerRepository labManagerRepository;


    /**
     * 현재 시간과 lab id를 통해서 방장인 Member 반환
     */
    @Override
    public MemberSimpleInfo searchMemberByLabId(Long labId) {
        return labManagerRepository.findMemberByLabId(labId, LocalDate.now())
                .map(MemberSimpleInfo::toMemberSimpleInfo)
                .orElse(null);
    }




}
