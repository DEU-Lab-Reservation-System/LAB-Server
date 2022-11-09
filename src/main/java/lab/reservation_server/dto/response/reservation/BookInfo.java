package lab.reservation_server.dto.response.reservation;

import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예약을 성공적으로 진행하고 나서 반환하는 response
 */
@Getter
@AllArgsConstructor
public class BookInfo {

    private Long id; // 예약 id

    private String name; // 예약자 이름

    private String userId; // 예약자 학번

    private String major; // 예약자 학과

    private String roomNumber;

    private String seatNum;

    private String startTime;

    private String endTime;

    private String extensionTime;

    private Boolean permission;

    public BookInfo(Reservation reservation, Lab lab , Member member){
        this.id = reservation.getId();
        this.name = member.getName();
        this.userId = member.getUserId();
        this.major = member.getMajor();
        this.roomNumber = lab.getRoomNumber();
        this.seatNum = reservation.getSeatNum();
        this.startTime = reservation.getStartTime().toString();
        this.endTime = reservation.getEndTime().toString();
        this.extensionTime = reservation.getExtensionTime().toString();
        this.permission = reservation.getPermission();
    }

    public BookInfo(Reservation reservation, Member member) {
        this.id = reservation.getId();
        this.name = member.getName();
        this.userId = member.getUserId();
        this.major = member.getMajor();
        this.roomNumber = reservation.getLab().getRoomNumber();
        this.seatNum = reservation.getSeatNum();
        this.startTime = reservation.getStartTime().toString();
        this.endTime = reservation.getEndTime().toString();
        this.extensionTime = reservation.getExtensionTime().toString();
        this.permission = reservation.getPermission();
    }

    public BookInfo(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getMember().getName();
        this.userId = reservation.getMember().getUserId();
        this.major = reservation.getMember().getMajor();
        this.roomNumber = reservation.getLab().getRoomNumber();
        this.seatNum = reservation.getSeatNum();
        this.startTime = reservation.getStartTime().toString();
        this.endTime = reservation.getEndTime().toString();
        this.extensionTime = reservation.getExtensionTime().toString();
        this.permission = reservation.getPermission();
    }
}
