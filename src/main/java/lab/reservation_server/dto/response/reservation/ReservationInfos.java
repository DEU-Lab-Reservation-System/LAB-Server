package lab.reservation_server.dto.response.reservation;

import java.util.ArrayList;
import java.util.List;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 예약한 내역 목록들을 반환할때 사용하는 response
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationInfos {

    private List<BookInfo> reservations = new ArrayList<>();

    public void addReservationInfo(List<Reservation> reservation, Member member) {
        for (Reservation res : reservation) {
            reservations.add(new BookInfo(res,member));
        }
    }

    public void addReservationInfo(List<Reservation> reservation) {
        for (Reservation res : reservation) {
            reservations.add(new BookInfo(res));
        }
    }
}
