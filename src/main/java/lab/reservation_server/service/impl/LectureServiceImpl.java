package lab.reservation_server.service.impl;

import java.util.List;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Lecture;
import lab.reservation_server.dto.request.LectureSaveDto;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.repository.LabRepository;
import lab.reservation_server.repository.LectureRepository;
import lab.reservation_server.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LabRepository labRepository;

    /**
     * 강의 시간표 추가
     */
    @Override
    @Transactional
    public void addLecture(List<LectureSaveDto> saveDtoList) {

      // lectureSaveDtoList를 순회하면서 lecture를 생성
      for (LectureSaveDto lectureSaveDto : saveDtoList) {
        Lab lab = labRepository.findByRoomNumber(lectureSaveDto.getRoomNumber())
              .orElseThrow(() -> new BadRequestException("해당 강의실이 존재하지 않습니다."));

        // check lecture between start time and end time on same day and same lab
        lectureRepository.checkDuplicate(lab.getRoomNumber(), lectureSaveDto.getDay(), lectureSaveDto.getStartTime(),lectureSaveDto.getEndTime())
            .ifPresent(lecture -> {
              throw new BadRequestException("해당 강의실에 해당 시간에 강의가 존재합니다.");
            });

        Lecture lecture = lectureSaveDto.toEntity(lectureSaveDto,lab);
        lectureRepository.save(lecture);
      }


    }



}
