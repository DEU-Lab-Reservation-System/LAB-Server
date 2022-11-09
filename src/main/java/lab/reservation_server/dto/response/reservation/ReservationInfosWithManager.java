package lab.reservation_server.dto.response.reservation;

import java.util.ArrayList;
import java.util.List;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationInfosWithManager {

  private List<BookInfo> reservations = new ArrayList<>();

  private MemberSimpleInfo manager = null;

  public void addReservationInfo(List<Reservation> reservation) {
    for (Reservation res : reservation) {
      reservations.add(new BookInfo(res));
    }
  }

  public void setManager(MemberSimpleInfo manager) {
    this.manager = manager;
  }

}
