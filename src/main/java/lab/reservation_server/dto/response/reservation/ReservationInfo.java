package lab.reservation_server.dto.response.reservation;

import java.time.LocalDateTime;
import java.util.List;
import lab.reservation_server.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 오늘의 예약 정보 반환할때 사용하는 response
 */
@Getter
@AllArgsConstructor
public class ReservationInfo {

    private Long id;

    private String roomNumber;

    private String seatNum;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime extensionTime;

    private Boolean permission;

    public ReservationInfo(Reservation reservation) {
        this.id = reservation.getId();
        this.roomNumber = reservation.getLab().getRoomNumber();
        this.seatNum = reservation.getSeatNum();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.extensionTime = reservation.getExtensionTime();
        this.permission = reservation.getPermission();
    }

    public static ReservationInfo toCurrentReservation(List<Reservation> reservations) {
        // 예약 내역이 존재하지 않으면 null
        if (reservations.isEmpty()) {
            return null;
        }else{
            // 가장 최근 예약 내역을 들고오는데, 이미 종료된 예약이면 null
            if (reservations.get(0).getEndTime().isBefore(LocalDateTime.now())) {
                return null;
            }
            // 현재 시간 기준으로 예약이 존재하거나 혹은 예정되어 있는 경우
            return new ReservationInfo(reservations.get(0));
        }
    }
}
