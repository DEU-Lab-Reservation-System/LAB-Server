package lab.reservation_server.dto.response.reservation;

import java.time.LocalDateTime;
import lab.reservation_server.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

}
