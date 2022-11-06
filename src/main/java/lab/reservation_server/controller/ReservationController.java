package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.response.reservation.BookInfo;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
     *
     * todo : 예약 시작 시간이 16시 반 전인 경우, 최대 시간도 16시반으로 제한해야 한다.
     */
    @PostMapping("/api/book")
    @ApiOperation(value="실습실 자리 예약" , notes = "실습실 자리를 예약할 수 있다.")
    public ResponseEntity<BookInfo> book(@RequestBody @Valid BookRequest book) {
      BookInfo bookInfo = reservationService.doReservation(book);
       return ResponseEntity.ok(bookInfo);
    }





}
