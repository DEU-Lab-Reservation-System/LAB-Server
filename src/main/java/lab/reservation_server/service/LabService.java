package lab.reservation_server.service;

import java.util.List;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.dto.response.lab.LectureInLab;
import lab.reservation_server.dto.response.lecture.LectureInfo;

public interface LabService {
  LectureInLab getLabTimeTable(String roomNumber);

  List<LectureInfo> getAllLabTimeTable();

  Lab findLabWithRoomNumber(String roomNumber);
}
