package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lab.reservation_server.dto.response.lab.LectureInLab;
import lab.reservation_server.dto.response.lecture.LectureInfo;
import lab.reservation_server.service.LabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "LabController : 강의실 시간표 조회 및 강의실 추가")
@Slf4j
public class LabController {

    private final LabService labService;

    @PostMapping("/api/lab")
    @ApiOperation(value="강의실 추가" , notes = "강의실을 추가할 수 있다.")
    public void addLab() {

    }

    /**
     * 강의실의 시간표 조회
     */
    @GetMapping("/api/labs/{roomNumber}")
    @ApiOperation(value="강의실 시간표 조회" , notes = "강의실의 시간표를 조회할 수 있다.")
    public ResponseEntity<LectureInLab> getLabTimeTable(@PathVariable("roomNumber") String roomNumber) {
        LectureInLab labTimeTable = labService.getLabTimeTable(roomNumber);
        return ResponseEntity.ok(labTimeTable);
    }

    /**
     * 강의실 전체에 대한 모든 시간표 조회
     */
    @GetMapping("/api/labs/timetable")
    @ApiOperation(value="모든 강의실 전체 시간표 조회" , notes = "강의실 전체에 대한 시간표를 조회할 수 있다.")
    public ResponseEntity<List<LectureInfo>> getAllLabTimeTable() {
        List<LectureInfo> allTimeTable = labService.getAllLabTimeTable();
        return ResponseEntity.ok(allTimeTable);
    }



}
