package lab.reservation_server.service;

import lab.reservation_server.dto.response.reservation.ReservationInfo;

public interface ReservationService {

  ReservationInfo getReservationFromMemberId(Long memberId);
}
