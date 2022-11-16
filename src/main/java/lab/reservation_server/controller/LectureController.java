package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lab.reservation_server.dto.request.lecture.LectureEditDto;
import lab.reservation_server.dto.request.lecture.LectureSaveDto;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.lecture.LectureInfo;
import lab.reservation_server.service.LabService;
import lab.reservation_server.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Api(tags = "Lecture Controller : 실습실 강의 시간표 관련, 시간표 추가 수정 삭제")
@Slf4j
public class LectureController {

    private final LectureService lectureService;
    private final LabService labService;

    /**
     * 강의 시간표 추가
     */
    @PostMapping("/api/lecture")
    @ApiOperation(value="강의 시간표 추가" , notes = "강의 시간표를 추가할 수 있다.")
    public ResponseEntity<List<LectureInfo>> addLecture(@RequestBody List<@Valid LectureSaveDto> saveDtoList) {
        List<LectureInfo> lectureInfos = lectureService.addLecture(saveDtoList);
        return ResponseEntity.ok(lectureInfos);
    }

    /**
     * 강의 시간표 수정
     * class 단에서 Validated를 선언하고
     * Valid를 객체에 적용하면, 500 ConstraintViolationException 가 발생한다.
     */
    @PutMapping("/api/lectures")
    @ApiOperation(value="강의 시간표 수정" , notes = "강의 시간표를 수정할 수 있다.")
    public ResponseEntity<?> updateLecture(@RequestBody List<@Valid LectureEditDto> lectures) {

        // 강의 코드 추출
        String code = lectures.get(0).getCode();

        // 강의 코드가 존재하는지 확인
        lectureService.checkIfCodeIsPresent(code);

        List<LectureEditDto> lectureEditDtos = lectureService.updateLecture(code, lectures);
        return ResponseEntity.ok(lectureEditDtos);
    }


    /**
     * 강의 시간표 삭제, 과목 code로 삭제
     */
    @DeleteMapping("/api/lectures/{code}")
    @ApiOperation(value="강의 시간표 삭제" , notes = "강의 시간표를 과목 코드로 삭제할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> deleteLecture(@PathVariable(value = "code") String code) {
        // 강의 코드가 존재하는지 확인
        lectureService.checkIfCodeIsPresent(code);
        lectureService.deleteLecture(code);
        return ResponseEntity.ok(new DefaultMessageResponse("강의 시간표 삭제 성공"));
    }


    /**
     * 강의실 전체에 대한 모든 시간표 조회
     */
    @GetMapping("/api/lectures")
    @ApiOperation(value="모든 강의실 전체 시간표 조회" , notes = "강의실 전체에 대한 시간표를 조회할 수 있다.")
    public ResponseEntity<List<LectureInfo>> getAllLabTimeTable() {
        List<LectureInfo> allTimeTable = labService.getAllLabTimeTable();
        return ResponseEntity.ok(allTimeTable);
    }


    /***
     * 교수가 세미나 등록
     */
    @PostMapping("/api/lectures/seminar")
    @ApiOperation(value="교수가 세미나 등록" , notes = "교수가 세미나를 등록할 수 있다.")
    public ResponseEntity<LectureInfo> addSeminar(@RequestBody @Valid LectureSaveDto seminarSaveDto) {
        LectureInfo seminar = lectureService.addSeminar(seminarSaveDto);
        return ResponseEntity.ok(seminar);
    }







}
