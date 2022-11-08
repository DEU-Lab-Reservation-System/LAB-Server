package lab.reservation_server.statepattern;

import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class DefaultNewVersionRoom {

    private final StateIterator stateIterator;

    private boolean flag = false;

    private RoomState roomState;

    public DefaultNewVersionRoom(StateIterator stateIterator) {
        this.stateIterator = stateIterator;;
        this.roomState = stateIterator.getNextRoomState();
    }

    /**
     * 강의실이 가득 찬 상태인지 확인한다.
     */
    public void checkIfRoomIsFull(BookRequest book) {
        // book request를 받아서 예약 가능한 강의실인지 확인
        String roomNum = book.getRoomNum();

        while (!flag){
          // 가득 차 있으면 true, 아니면 false
          if (roomState.checkIfRoomIsFull(roomNum)) {
              // 다음 강의실 상태로 넘어간다.
              if (stateIterator.hasNextRoomState()) {
                // 918이 맨자미작으로 위의 첫번째 if문으로 들어왔으면 state에는 비어있는 상태
                roomState = stateIterator.getNextRoomState();
              }else{
                  // 918까지 모두 예약이 되어서 자리가 없는 상태이다.
                  stateIterator.initRoomStates();
                  this.roomState = stateIterator.getNextRoomState();
                  throw new BadRequestException("모든 강의실이 포화 상태입니다. 다음에 이용해주세요");
              }
          }else {
            // 예약이 가능한 강의실이다. true로 바꿔서 while문을 빠져나간다.
            flag = true;
          }
        }// end of while
        flag = false; // flag 다시 초기화 해서 다음 요청이 올때 강의실이 포화상태는 아닌지 확인한다.
      }


}
