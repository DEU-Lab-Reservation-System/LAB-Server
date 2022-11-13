package lab.reservation_server.service.impl;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.LabManager;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.repository.LabManagerRepository;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.repository.ReservationRepository;
import lab.reservation_server.service.LabManagerService;
import lab.reservation_server.service.firebase.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabManagerImpl implements LabManagerService {

    private final LabManagerRepository labManagerRepository;

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    private final FirebaseCloudMessageService firebaseCloudMessageService;


    /**
     * 현재 시간과 lab id를 통해서 해당 강의실을 담당하고 있는 방장 Member 반환
     */
    @Override
    public MemberSimpleInfo searchMemberByLabId(Long labId) {
        return labManagerRepository.findMemberByLabId(labId, LocalDate.now())
                .map(MemberSimpleInfo::toMemberSimpleInfo)
                .orElse(null);
    }

    /**
     * 이미 17시 이후의 예약 내역은 있는 상태
     * 17 이후의 사용자에 대한 방장 데이터가 없으면 insert, 있으면 update
     */
    @Override
    @Transactional
    public void updateLabManager(Lab lab, List<Long> reservationIds) throws IOException {

        Optional<List<Reservation>> reservations =
            reservationRepository.findMemberWithLongestTime(lab, reservationIds);

        // ids가 모두 존재하는지 확인
        if(reservations.isPresent()){
            // 승인될 목록 중에서 가장 오랫동안 있는 사람 (새롭게 방장이 될 가능성이 있는 사람)
            Member member = reservations.get().get(0).getMember();

            // 기존의 방장
            Optional<LabManager> labManager =
                labManagerRepository.findLabManagerByLabIdAndDate(lab, LocalDate.now());

            // 이미 방장이 있는 경우
            if(labManager.isPresent()){
                // 실제 방장이 존재할 시간이랑 예비 방장이랑 누가 더 오래 있는지 판단해야 한다.
                Reservation reservationOriManager = reservationRepository.findReservationByMemberAndLab(
                    labManager.get().getMember(),
                    lab, Date.valueOf(LocalDate.now())).get(0);

                Reservation newReservation = reservations.get().get(0);

                // 예비 방장이 더 오래 있을 경우, 방장 업데이트
                if(reservationOriManager.getEndTime().isBefore(newReservation.getEndTime())){
                    String deviceTokenForNewManager = member.getDeviceToken();

                    labManager.get().updateMember(member);

                    firebaseCloudMessageService.sendMessageTo(deviceTokenForNewManager,
                        "방장 업데이트 알림", "해당 강의실의 방장이 되셨습니다.");

                    firebaseCloudMessageService.sendMessageTo(reservationOriManager.getMember().getDeviceToken(),
                        "방장 업데이트 알림", "새로운 사람으로 방장이 변경 되었습니다.");
                }
            }
            else{
                // 기존 방장이 없는 경우, 현재 승인 목록 중 가장 오래 있는 사람이 방장으로 지정
                labManagerRepository.save(new LabManager(member, lab));

                String deviceTokenForNewManager = member.getDeviceToken();
                firebaseCloudMessageService.sendMessageTo(deviceTokenForNewManager,
                    "방장 업데이트 알림", "방장이 되셨습니다.");

            }
        }else{
            // ids 목록들이 모두 존재하지 않는 경우
            throw new BadRequestException("모두 존재하지 않는 reservationIds 입니다.");
        }
    }


}
