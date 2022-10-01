package lab.reservation_server.service.impl;


import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.repository.ReservationRepository;
import lab.reservation_server.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * member의 id를 통해서 예약 정보를 가져온다.
     */
      @Override
      public ReservationInfo getReservationFromMemberId(Long memberId) {

        // 값이 있으면, reservationInfo를 만들어서 반환, 없으면 null 반환
        ReservationInfo reservationInfo =
            reservationRepository.findReservationByMemberId(memberId).map(ReservationInfo::new)
                .orElse(null);

        return reservationInfo;
      }
}
