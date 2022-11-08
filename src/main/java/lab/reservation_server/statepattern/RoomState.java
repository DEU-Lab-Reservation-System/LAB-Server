package lab.reservation_server.statepattern;

/**
 * 각각의 실습실 상태에 따라서 포화 상태이면 각기 다른 error message를 반환해주기 위해
 */
public interface RoomState {

   boolean checkIfRoomIsFull(String roomNum);
}
