package lab.reservation_server.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.LabManager;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lab.reservation_server.repository.LabManagerRepository;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.repository.ReservationRepository;
import lab.reservation_server.service.LabManagerService;
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
    public void updateLabManager(Lab lab, BookRequest book) {

        // 17시 이후의 예약 내역 중에서 가장 늦게 끝나는 순으로 정렬 후 찾기
        List<Reservation> reservations = reservationRepository.findReservationWithPermissionByLabId(lab, Date.valueOf(
                LocalDate.now()), false).orElse(null);

        // false 목록 중에서 가장 늦게 끝나는 예약 내역, 곧 방장
        Reservation reservation = reservations.get(0);

        // 이미 방장 데이터가 있는지 확인
        Optional<LabManager> labManagerByLabIdAndDate =
            labManagerRepository.findLabManagerByLabIdAndDate(lab, LocalDate.now());

        if(labManagerByLabIdAndDate.isPresent()){
            //update, 이미 누군가 방장을 맡고 있다는 이야기 -> 가장 늦게 끝나는 예약을 방장으로 새롭게 업데이트
            labManagerByLabIdAndDate.get().updateMember(reservation.getMember());
        }else{
            //insert, 아직 아무도 방장을 맡지 않는 경우 (즉, 오후반에 대해서 맨 처음으로 예약을 한 사람이 초기에 방장에 된다.)
            labManagerRepository.save(new LabManager(reservation.getMember(),lab));
        }


    }


}
