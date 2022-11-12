package lab.reservation_server.statepattern;

import java.time.LocalDate;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.repository.LabRepository;
import lab.reservation_server.repository.ReservationRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class ConcreteRoomState implements RoomState {

    private int maxCapacity; // 강의실의 최대 인원
    private int currentCapacity; // 현재 예약 잡힌 인원
    private String roomNum; // 강의실 번호
    private int deadline; // 여분의 자리

    private final ReservationRepository reservationRepository;
    private final LabRepository labRepository;


    public ConcreteRoomState(ReservationRepository reservationRepository, LabRepository labRepository, String roomNum, int deadline) {
        this.reservationRepository = reservationRepository;
        this.labRepository = labRepository;
        this.roomNum = roomNum;
        this.deadline = deadline;
        setMaxCapacity(roomNum);
    }

    private void getCurrentCapacity(ReservationRepository reservationRepository) {
        this.currentCapacity =  reservationRepository.countCurrentCapacity(roomNum,java.sql.Date.valueOf(
            LocalDate.now()));
        log.info("current capacity {} 현재 이용자수 : {}", roomNum, currentCapacity);
    }


    private void setMaxCapacity(String roomNum) {
        this.maxCapacity = labRepository.findMaxCapacity(roomNum);
    }

    @Override
    public boolean checkIfRoomIsFull(String roomNum) {
        // 예약 목록 중애서 남은 자리가 5개 이하면 다음 강의실로 예약할 것을 알려준다.
        // 제일 최근 예약 업데이트해서 현재 이용중인 좌석 업데이트하기
        getCurrentCapacity(reservationRepository);

        // 우선순위 높은 강의실이 5자리 보다 많이 남은 경우
        if (maxCapacity - currentCapacity > deadline) {
            if (roomNum.equals(this.roomNum)) {
                log.info("현재 강의실 {} 이용 가능", roomNum);
                return false;
            } else {
                // 911에 자리가 많지만 다른 방에 자리를 잡은 경우
                log.info("현재 강의실 {} 먼저 이용 부탁드립니다.", this.roomNum);
                throw new BadRequestException(this.roomNum + " 강의실 이용 부탁드립니다.");
            }
        }else{
            log.info("다음 강의실로 예약 부탁드립니다.");
            //throw new BadRequestException("다음 강의실로 예약 부탁드립니다.");
            return true;
        }
    }



}
