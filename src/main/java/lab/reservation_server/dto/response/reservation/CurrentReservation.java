package lab.reservation_server.dto.response.reservation;

import java.util.List;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 현 시간부로 해당 강의실에 예약 되어 있는 (사용중인 좌석 목록 확인)
 */
@Getter
@AllArgsConstructor
public class CurrentReservation {

    private List<String> seatList;

    private MemberSimpleInfo manager;
}
