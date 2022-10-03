package lab.reservation_server.service.impl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Lecture;
import lab.reservation_server.dto.request.LectureEditDto;
import lab.reservation_server.dto.request.LectureSaveDto;
import lab.reservation_server.dto.response.lecture.LectureInfo;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.exception.LecturePresentException;
import lab.reservation_server.repository.LabRepository;
import lab.reservation_server.repository.LectureRepository;
import lab.reservation_server.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LabRepository labRepository;

    /**
     * 강의 시간표 추가
     */
    @Override
    @Transactional
    public List<LectureInfo> addLecture(List<LectureSaveDto> saveDtoList) {

      List<LectureInfo> lectures = new ArrayList<>();

      // lectureSaveDtoList를 순회하면서 lecture를 생성
      for (LectureSaveDto lectureSaveDto : saveDtoList) {
        Lab lab = checkIfLabPresent(lectureSaveDto.getRoomNumber());

        checkDuplicateSchedule(lab.getRoomNumber(), lectureSaveDto.getDay(),
            lectureSaveDto.getStartTime(), lectureSaveDto.getEndTime());

        Lecture lecture = lectureSaveDto.toEntity(lectureSaveDto, lab);
        lectureRepository.save(lecture);
        // roomNumber를 가지고 있는 LectureInfo로 변환해서 list에 담아준다.
        lectures.add(new LectureInfo(lecture, lab.getRoomNumber()));
      }

      return lectures;
    }


  /**
     * 강의 시간표 수정
     */
    @Override
    @Transactional
    public void updateLecture(String code, List<LectureEditDto> lectures) {

      // delete all lecture with code
      lectureRepository.deleteAllByCode(code);

      // lectureEditDtoList를 순회하면서 lecture를 생성
      for (LectureEditDto lectureEditDto : lectures) {

        Lab lab = checkIfLabPresent(lectureEditDto.getRoomNumber());

        // check lecture between start time and end time on same day and same lab
        checkDuplicateSchedule(lab.getRoomNumber(), lectureEditDto.getDay(),
            lectureEditDto.getStartTime(), lectureEditDto.getEndTime());

        Lecture lecture = lectureEditDto.toEntity(lectureEditDto,lab,code);
        lectureRepository.save(lecture);
      }



    }

    /**
     * code로 강의 시간표 삭제
     */
    @Override
    @Transactional
    public void deleteLecture(String code) {
      lectureRepository.deleteAllByCode(code);
    }

    /**
     * 과목 코드가 존재하는지 확인
     * @param code
     */
    @Override
    public void checkIfCodeIsPresent(String code) {
      if (!lectureRepository.existsByCode(code)) {
        throw new BadRequestException("존재하지 않는 과목 코드입니다.");
      }
    }

    /**
     * 현재 시간에 강의가 있는지 확인
     */
    @Override
    public void checkLectureNow(Lab lab, LocalDateTime now) {
      // 현재 시간에 강의가 있는지 확인
        if (lectureRepository.checkNowByLabId(lab,
            now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
            now.toLocalTime()).isPresent()) {
            throw new LecturePresentException("현재 시간에 강의가 있습니다.");
        }
    }


     /**
     * roomNumber로 lab을 찾아서 반환
     */
    private Lab checkIfLabPresent(String roomNumber) {
        Lab lab = labRepository.findByRoomNumber(roomNumber)
            .orElseThrow(() -> new BadRequestException("해당 강의실이 존재하지 않습니다."));
        return lab;
      }

    /**
     * 추가 혹은 수정하고자 하는 강의 시간표에서 겹치지 않는지 확인
     */
    private void checkDuplicateSchedule(String roomNumber, String day, LocalTime startTime, LocalTime endTime) {
      // check lecture between start time and end time on same day and same lab
      lectureRepository.checkDuplicate(roomNumber, day, startTime, endTime)
          .ifPresent(lecture -> {
            throw new BadRequestException("해당 강의실에 해당 시간에 강의가 존재합니다.");
          });
    }


}
