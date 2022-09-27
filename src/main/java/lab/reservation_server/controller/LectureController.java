package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lab.reservation_server.dto.request.LectureSaveDto;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Lecture Controller : 실습실 강의 시간표 관련")
@Slf4j
public class LectureController {

    private final LectureService lectureService;

    /**
     * 강의 시간표 추가
     */
    @PostMapping("/api/lecture")
    @ApiOperation(value="강의 시간표 추가" , notes = "강의 시간표를 추가할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> addLecture(@RequestBody @Valid List<LectureSaveDto> saveDtoList) {
        lectureService.addLecture(saveDtoList);
        return ResponseEntity.ok(new DefaultMessageResponse("강의 시간표 추가 성공"));
    }








}
