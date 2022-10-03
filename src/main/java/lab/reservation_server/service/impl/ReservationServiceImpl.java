package lab.reservation_server.service.impl;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.repository.ReservationRepository;
import lab.reservation_server.service.LabService;
import lab.reservation_server.service.LectureService;
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
    private final LabService labService;
    private final LectureService lectureService;

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

      /**
       * 현재 시간 기준으로 해당 강의실에 강의가 있는지 확인, 없으면 현 사용중인 좌석 반환
       */
      @Override
      public CurrentReservation checkReservation(String roomNumber) {

        Lab lab = labService.findLabWithRoomNumber(roomNumber);
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간 기준으로 해당 강의실에 수업이 있는지 확인, 있으면 BadRequestException 내부적으로 반환
        lectureService.checkLectureNow(lab,now);

        // 현재 시간 기준으로 해당 강의실에 수업이 없으면, 현재 이용중인 좌석 반환
        List<Reservation> reservations = reservationRepository.findCurrentReservation(lab,now).orElse(null);

        // reservations의 seatNumber를 List로 반환
        List<String> seatNums = reservations.stream()
            .map(Reservation::getSeatNum)
            .collect(Collectors.toList());

        return new CurrentReservation(seatNums);
      }
}