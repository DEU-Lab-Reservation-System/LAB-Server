package lab.reservation_server.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Lecture;
import lab.reservation_server.dto.request.lecture.LectureEditDto;
import lab.reservation_server.dto.request.lecture.LectureSaveDto;
import lab.reservation_server.dto.response.lecture.LectureInfo;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.exception.LecturePresentException;
import lab.reservation_server.repository.LabRepository;
import lab.reservation_server.repository.LectureRepository;
import lab.reservation_server.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            lectureSaveDto.getStartTime(), lectureSaveDto.getEndTime(), lectureSaveDto.getStartDate(),lectureSaveDto.getEndDate());

        Lecture lecture = lectureSaveDto.toEntity(lectureSaveDto, lab);
        lectureRepository.save(lecture);
        // roomNumber를 가지고 있는 LectureInfo로 변환해서 list에 담아준다.
        lectures.add(new LectureInfo(lecture, lab.getRoomNumber()));
      }

      return lectures;
    }


  /**
     * 강의 시간표 수정
     * https://interconnection.tistory.com/122
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<LectureEditDto> updateLecture(String code, List<LectureEditDto> lectures) {

      try{
        // delete all lecture with code
        lectureRepository.deleteAllByCode(code);

        // lectureEditDtoList를 순회하면서 lecture를 생성
        for (LectureEditDto lectureEditDto : lectures) {

          Lab lab = checkIfLabPresent(lectureEditDto.getRoomNumber());

          // check lecture between start time and end time on same day and same lab
          checkDuplicateSchedule(lab.getRoomNumber(), lectureEditDto.getDay(),
              lectureEditDto.getStartTime(), lectureEditDto.getEndTime(), lectureEditDto.getStartDate(),lectureEditDto.getEndDate());

          Lecture lecture = lectureEditDto.toEntity(lectureEditDto, lab, code);
          lectureRepository.save(lecture);
        }
      } catch (Exception e) {
        // 알 수 없는 문제로 강의 시간표 수정 실패 (rollback)
        log.warn("강의 시간표 수정 실패되어 rollback 합니다.");
        throw new BadRequestException("오류가 발생하여 강의 시간표 수정에 실패하였습니다.");
      }

      return lectures;
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

      // get day of week now in korean
      String dayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

      // extract only hour and minute and second
      LocalTime nowTime1 = LocalTime.of(now.getHour(), now.getMinute(), now.getSecond());

      // 현재 시간에 강의가 있는지 확인
      if (lectureRepository.checkNowByLabId(lab,dayOfWeek,nowTime1,LocalDate.now()).isPresent()) {
        throw new LecturePresentException("현재 시간에 강의가 있습니다.");
      }
    }


    /**
     * 특정 강의실이 특정 시간대에 수업중인지 확인 (해당 학기에 개설된 강의만 찾는다.)
     */
    @Override
    public void checkLectureBetweenTime(Lab lab, LocalTime startTime, LocalTime endTime) {

        if (lectureRepository.checkNowByLabIdBetweenTime(lab,
            LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN)
            , startTime
            , endTime
            , LocalDate.now()).get().size() > 0) {
          throw new LecturePresentException("해당 시간에 강의가 있습니다.");
        }

    }

    /**
     * 교수가 세미나를 등록할 수 있다.
     */
    @Override
    @Transactional
    public LectureInfo addSeminar(LectureSaveDto seminarSaveDto) {

        lectureRepository.findByCodeWithDate(seminarSaveDto.getCode(), LocalDate.now()).ifPresent(lecture -> {
          throw new BadRequestException("이미 교수님의 세미나가 존재합니다.");
        });

        checkDuplicateSchedule(seminarSaveDto.getRoomNumber(), seminarSaveDto.getDay(),
          seminarSaveDto.getStartTime(), seminarSaveDto.getEndTime(), seminarSaveDto.getStartDate(),seminarSaveDto.getEndDate());

        Lecture lecture = seminarSaveDto.toEntity(seminarSaveDto,
          checkIfLabPresent(seminarSaveDto.getRoomNumber()));

        lectureRepository.save(lecture);

        return new LectureInfo(lecture, lecture.getLab().getRoomNumber());
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
    private void checkDuplicateSchedule(String roomNumber, String day, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
      // check lecture between start time and end time on same day and same lab
      if (lectureRepository.checkDuplicate(roomNumber, day, startTime, endTime,startDate,endDate).get().size() > 0) {
        throw new BadRequestException("강의 시간표가 겹칩니다.");
      }
    }


}
