package lab.reservation_server.service;

import lab.reservation_server.dto.response.lab.LectureInLab;

public interface LabService {
  LectureInLab getLabTimeTable(String roomNumber);
}
