package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.validation.Valid;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.request.reservation.ExtendRequest;
import lab.reservation_server.dto.request.reservation.PermissionUpdate;
import lab.reservation_server.dto.request.reservation.RoomAndTime;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.reservation.BookInfo;
import lab.reservation_server.dto.response.reservation.ReservationInfos;
import lab.reservation_server.dto.response.reservation.ReservationInfosWithManager;
import lab.reservation_server.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Reservation Controller : 예약 관련")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 실습실 예약
     * todo : 예약 시작 시간이 16시 반 전인 경우, 최대 시간도 16시반으로 제한해야 한다.
     */
    @PostMapping("/api/reservation")
    @ApiOperation(value="실습실 자리 예약" , notes = "실습실 자리를 예약할 수 있다.")
    public ResponseEntity<BookInfo> book(@RequestBody @Valid BookRequest book) {
      BookInfo bookInfo = reservationService.doReservation(book);
       return ResponseEntity.ok(bookInfo);
    }

    /**
     * 사용자가 예약한 내역을 모드 조회할 수 있다.
     */
    @GetMapping("/api/reservations/{userId}")
    @ApiImplicitParam(name = "userId" , value = "사용자 아이디" , required = true)
    @ApiOperation(value="내 예약 조회" , notes = "내 예약 정보를 모두 조회할 수 있다.")
    public ResponseEntity<ReservationInfos> getReservationFromMemberId(@PathVariable String userId) {
      ReservationInfos infos = reservationService.getAllReservationFromMemberId(userId);
      return ResponseEntity.ok(infos);
    }

    /**
     * 조교가 17시 이후에 사용하고자 하는 미승인 예약 내역을 조회할 수 있다.
     */
    @GetMapping("/api/reservations/unauthorized")
    @ApiOperation(value="17시 이후 예약 조회" , notes = "조교는 17시 이후 승인되지 않은 예약 내역을 조회할 수 있다.")
    public ResponseEntity<ReservationInfos> getUnauthorizedReservation() {
      ReservationInfos infos = reservationService.getUnauthorizedReservation();
      return ResponseEntity.ok(infos);
    }


    /**
     * 특정 강의실, 특정 시간에 승인된 예약 현황을 확인할 수 있다.
     */
    @PostMapping("/api/reservations/list")
    @ApiOperation(value="특정 강의실 승인된 예약 현황(사용자) 조회" , notes = "특정 강의실, 특정 시간에 승인된 예약 현황(사용자)을 확인할 수 있다.")
    public ResponseEntity<ReservationInfosWithManager> getReservationFromRoomNumber(@RequestBody @Valid RoomAndTime roomAndTime) {
      ReservationInfosWithManager infos = reservationService.getReservationFromRoomNumber(roomAndTime);
      return ResponseEntity.ok(infos);
    }

    /**
     * 미승인된 예약 내역에 대해서 승인 혹은 거절을 할 수 있다.
     */
    @PostMapping("/api/reservations/authorize")
    @ApiOperation(value="예약 승인" , notes = "미승인된 예약 내역에 대해서 승인 혹은 거절을 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> authorizeReservation(@RequestBody @Valid PermissionUpdate permissionUpdate)
        throws IOException {
      String infos = reservationService.updatePermission(permissionUpdate);
      return ResponseEntity.ok(new DefaultMessageResponse(infos));
    }

    /**
     * 예약 연장
     */
    @PostMapping("/api/reservations/extend")
    @ApiOperation(value="예약 연장" , notes = "예약을 연장할 수 있다.")
    public ResponseEntity<BookInfo> extendReservation(@RequestBody @Valid ExtendRequest extendRequest)
        throws IOException {
      BookInfo info = reservationService.extendReservation(extendRequest);
      return ResponseEntity.ok(info);
    }


}
