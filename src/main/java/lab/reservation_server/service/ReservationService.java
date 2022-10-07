package lab.reservation_server.service;

import lab.reservation_server.dto.request.TimeStartToEnd;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
import lab.reservation_server.dto.response.reservation.ReservationInfo;

public interface ReservationService {

  ReservationInfo getReservationFromMemberId(Long memberId);

  CurrentReservation checkReservation(String roomNumber);

  CurrentReservation checkReservationBetweenTime(String roomNumber, TimeStartToEnd timeStartToEnd);
}
