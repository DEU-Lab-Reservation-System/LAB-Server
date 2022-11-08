package lab.reservation_server.statepattern;

import java.util.Deque;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import lab.reservation_server.repository.LabRepository;
import lab.reservation_server.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 각각의 실습실 상태에 따라서 포화 상태이면 각기 다른 error message를 반환해주기 위해
 * 실습실 우선순위를 설정할 수 있다.
 * 이용 가능한 실습실ㅇ르 concreteRoomState를 통해서 생성한다.
 */
@Component
@Slf4j
public class StateIterator {

    private Deque<RoomState> roomStates = new LinkedList<>();
    private final LabRepository labRepository;
    private final ReservationRepository reservationRepository;


    public StateIterator(LabRepository labRepository, ReservationRepository reservationRepository) {
        this.labRepository = labRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * 실습실 우선순위를 설정할 수 있다.
     */
    @PostConstruct
    public void initRoomStates() {
         roomStates.add(new ConcreteRoomState(reservationRepository,labRepository,"911",5));
         roomStates.add(new ConcreteRoomState(reservationRepository,labRepository,"915",5));
         roomStates.add(new ConcreteRoomState(reservationRepository,labRepository,"916",5));
         roomStates.add(new ConcreteRoomState(reservationRepository,labRepository,"918",5));
    }

    /**
     * 우선순위가 높은 실습실 상태를 순차적으로 반환한다.
     */
    public RoomState getNextRoomState() {
        RoomState roomState = roomStates.pollFirst();
        ConcreteRoomState roomState1 = (ConcreteRoomState) roomState;
        log.info("roomState 이 지금 deque에서 나갔음: {}",roomState1.getRoomNum());
        return roomState;
    }

    public boolean hasNextRoomState() {
      return !roomStates.isEmpty();
    }

    public void addRoomState(RoomState roomState) {
        roomStates.add(roomState);
    }

    public boolean isEmpty() {
        return roomStates.isEmpty();
    }
}
