package lab.reservation_server.dto.response.lab;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실습실의 강의 시간표를 반환
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LectureInLab {

  private String roomNumber;
  private List<LectureInfo> lectures;

}
