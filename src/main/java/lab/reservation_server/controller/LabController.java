package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lab.reservation_server.dto.request.reservation.TimeStartToEnd;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
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
@Api(tags = "LabController : 강의실 시간표 조회 및 강의실 추가 및 강의실 현황 확인")
@Slf4j
public class LabController {
    private final ReservationService reservationService;

    @PostMapping("/api/lab")
    @ApiOperation(value="강의실 추가" , notes = "강의실을 추가할 수 있다.")
    public void addLab() {

    }

    /**
     * 강의실의 시간표 조회
     */
//    @GetMapping("/api/labs/{roomNumber}")
//    @ApiOperation(value="강의실 시간표 조회" , notes = "강의실의 시간표를 조회할 수 있다.")
//    public ResponseEntity<LectureInLab> getLabTimeTable(@PathVariable("roomNumber") String roomNumber) {
//        LectureInLab labTimeTable = labService.getLabTimeTable(roomNumber);
//        return ResponseEntity.ok(labTimeTable);
//    }





    /**
     * 현재 시간 기준으로 강의실에 강의가 있는지 확인, 없으면 현 사용중인 좌석 반환
     */
    @GetMapping("/api/labs/{roomNumber}")
    @ApiOperation(value="현재 시간 기준으로 강의실에 강의가 있는지 확인, 없으면 현 사용중인 좌석 반환" ,
        notes = "현재 시간 기준으로 강의실에 강의가 있는지 확인, 없으면 현 사용중인 좌석 반환")
    public ResponseEntity<CurrentReservation> checkReservation(@PathVariable("roomNumber") String roomNumber) {
        CurrentReservation currentReservation = reservationService.checkReservation(roomNumber);
        return ResponseEntity.ok(currentReservation);
    }


    /**
     * 강의실 번호별로, 특정 시간대의 예약한 좌석 번호 반환
     */
    @PostMapping("/api/labs/{roomNumber}")
    @ApiOperation(value="강의실 번호별로, 특정 시간대의 예약한 좌석 번호 반환" ,
        notes = "강의실 번호별로, 특정 시간대의 예약한 좌석 번호 반환")
    public ResponseEntity<CurrentReservation> getReservationFromLab(@PathVariable("roomNumber") String roomNumber,
                                                                    @RequestBody
                                                                    TimeStartToEnd timeStartToEnd) {
        CurrentReservation currentReservation = reservationService.checkReservationBetweenTime(roomNumber, timeStartToEnd);
        return ResponseEntity.ok(currentReservation);
    }






}
