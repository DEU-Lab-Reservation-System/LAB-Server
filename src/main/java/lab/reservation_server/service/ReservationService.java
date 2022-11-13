package lab.reservation_server.service;

import java.io.IOException;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.request.reservation.ExtendRequest;
import lab.reservation_server.dto.request.reservation.PermissionUpdate;
import lab.reservation_server.dto.request.reservation.RoomAndTime;
import lab.reservation_server.dto.request.reservation.TimeStartToEnd;
import lab.reservation_server.dto.response.reservation.BookInfo;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.dto.response.reservation.ReservationInfos;
import lab.reservation_server.dto.response.reservation.ReservationInfosWithManager;

public interface ReservationService {

  ReservationInfo getCurrentReservationFromMemberId(Long memberId);

  CurrentReservation checkReservation(String roomNumber);

  CurrentReservation checkReservationBetweenTime(String roomNumber, TimeStartToEnd timeStartToEnd);

  BookInfo doReservation(BookRequest book);

  ReservationInfos getAllReservationFromMemberId(String userId);

  ReservationInfos getUnauthorizedReservation();

  ReservationInfosWithManager getReservationFromRoomNumber(RoomAndTime roomAndTime);

  String updatePermission(PermissionUpdate permissionUpdate) throws IOException;

  BookInfo extendReservation(ExtendRequest extendRequest) throws IOException;
}
