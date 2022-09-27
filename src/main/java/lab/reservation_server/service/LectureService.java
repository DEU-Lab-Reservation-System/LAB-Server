package lab.reservation_server.service;

import java.util.List;
import lab.reservation_server.dto.request.LectureSaveDto;

public interface LectureService {
  void addLecture(List<LectureSaveDto> saveDtoList);
}
