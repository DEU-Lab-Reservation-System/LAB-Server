package lab.reservation_server.service;

import java.time.LocalDateTime;
import java.util.List;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.dto.request.LectureEditDto;
import lab.reservation_server.dto.request.LectureSaveDto;
import lab.reservation_server.dto.response.lecture.LectureInfo;

public interface LectureService {
  List<LectureInfo> addLecture(List<LectureSaveDto> saveDtoList);

  void updateLecture(String code, List<LectureEditDto> lectures);

  void deleteLecture(String code);

  void checkIfCodeIsPresent(String code);

  void checkLectureNow(Lab lab, LocalDateTime now);
}
