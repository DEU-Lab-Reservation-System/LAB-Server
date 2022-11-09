package lab.reservation_server.service.impl;


import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;;
import java.util.Optional;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.request.reservation.RoomAndTime;
import lab.reservation_server.dto.request.reservation.TimeStartToEnd;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lab.reservation_server.dto.response.reservation.BookInfo;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
import lab.reservation_server.dto.response.reservation.ReservationInfos;
import lab.reservation_server.dto.response.reservation.ReservationInfosWithManager;
import lab.reservation_server.exception.AlreadyBookedException;
import lab.reservation_server.exception.BadRequestException;
import lab.reservation_server.exception.FullOfCapacityException;
import lab.reservation_server.repository.MemberRepository;
import lab.reservation_server.repository.ReservationRepository;
import lab.reservation_server.service.LabManagerService;
import lab.reservation_server.service.LabService;
import lab.reservation_server.service.LectureService;
import lab.reservation_server.service.ReservationService;
import lab.reservation_server.statepattern.DefaultNewVersionRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final LabService labService;
    private final LectureService lectureService;
    private final LabManagerService labManagerService;

    private final MemberRepository memberRepository;

    private final DefaultNewVersionRoom defaultNewVersionRoom;

    /**
     * member의 id를 통해서 <b>가장 최근의</b> 예약 정보를 가져온다.
     */
      @Override
      public ReservationInfo getCurrentReservationFromMemberId(Long memberId) {

        LocalDateTime now = LocalDateTime.now();

        // 값이 있으면, reservationInfo를 만들어서 반환, 없으면 null 반환
        // find first of list and conver to ReservationInfo
        ReservationInfo reservationInfo =
            reservationRepository.findReservationByMemberId(memberId,now).map(ReservationInfo::toCurrentReservation)
                .orElse(null);

        return reservationInfo;
      }

      /**
       * 현재 시간 기준으로 해당 강의실에 강의가 있는지 확인, 없으면 현 사용중인 좌석 반환 (사용자 화면 기준 - 메인 화면)
       */
      @Override
      public CurrentReservation checkReservation(String roomNumber) {

        Lab lab = labService.findLabWithRoomNumber(roomNumber);
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간 기준으로 해당 강의실에 수업이 있는지 확인, 있으면 BadRequestException 내부적으로 반환
        checkLectureNow(lab, now);

        // 현재 시간 기준으로 해당 강의실에 수업이 없으면, 현재 이용중인 좌석 반환
        List<Reservation> reservations = reservationRepository.findCurrentReservation(lab,now).orElse(null);

        // 현재 날짜 기준와 lab실 정보를 통해서 현재 방장을 찾는다.
        MemberSimpleInfo memberSimpleInfo = labManagerService.searchMemberByLabId(lab.getId());


        // reservations의 seatNumber를 List로 반환
        List<String> seatNums = reservations.stream()
            .map(Reservation::getSeatNum)
            .collect(Collectors.toList());

        return new CurrentReservation(seatNums, memberSimpleInfo,false);
      }

      /**
       * 현재 시간 기준으로 해당 강의실에 수업이 있는지 확인, 있으면 BadRequestException 내부적으로 반환
       */
      private void checkLectureNow(Lab lab, LocalDateTime now) {
            lectureService.checkLectureNow(lab, now);
      }

    /**
     * 특정 강의실, 특정 시간대에 예약 현황과 해당 강의실 방장 정보를 가져온다.
     */
    @Override
    public CurrentReservation checkReservationBetweenTime(String roomNumber, TimeStartToEnd timeStartToEnd) {

      // 강의실 데이터 조회
      Lab lab = labService.findLabWithRoomNumber(roomNumber);

      // 시간 범위안에서 강의실에 수업이 있는지 확인, 있으면 LecturePresentException 내부적으로 반환
      lectureService.checkLectureBetweenTime(lab,timeStartToEnd.getStartTime(),timeStartToEnd.getEndTime());

      // 시간 범위 안으로 이용중인 예약 현황을 보여준다.
      List<Reservation> reservations =
          reservationRepository.findCurrentReservationBetweenTime
              (lab, LocalDateTime.of(LocalDate.now(), timeStartToEnd.getStartTime()),
                  LocalDateTime.of(LocalDate.now(), timeStartToEnd.getEndTime()),
                  java.sql.Date.valueOf(LocalDate.now())).orElse(null);

      // 현재 날짜 기준와 lab실 정보를 통해서 해당 강의실의 방장 데이터를 가져온다.
      MemberSimpleInfo memberSimpleInfo = labManagerService.searchMemberByLabId(lab.getId());

      // seatNums를 추출
      List<String> seatNums = reservations.stream()
          .map(Reservation::getSeatNum)
          .collect(Collectors.toList());

      return new CurrentReservation(seatNums, memberSimpleInfo,false);
    }

    /**
     * 최종적으로 해당 좌석이 이용 가능한지 확인 후, 예약을 진행한다.
     * @param book 예약 요청 정보
     * @return 예약 완료 정보
     */
    @Transactional
    @Override
    public BookInfo doReservation(BookRequest book) {

        // 이용하고자 하는 사용자 데이터 조회
        Member member = memberRepository.findByUserId(book.getUserId())
            .orElseThrow(() -> new BadRequestException("해당 사용자가 존재하지 않습니다."));

        // 이용하고자 하는 강의실 데이터 조회
        Lab lab = labService.findLabWithRoomNumber(book.getRoomNum());

        // book의 예약 시작 시간이 16시 30분 전, 16시 30분 이후로 나누어서 예약을 진행한다.
        boolean beforeTime = checkIfBookStartBeforeTime(book);

        // 실습실 정보, 예약 시작 시간, 예약 종료 시간, 예약하고자 하는 날짜를 통해서 판단해야 한다.
        lectureService.checkLectureBetweenTime(lab, book.getStartTime().toLocalTime(), book.getEndTime().toLocalTime());

        // 팀 인원이 현재 강의실 인원보다 많으면 예약 불가
        checkIfTeamSizeIsBiggerThanCapacity(book,lab);

        // 해당 사용자가 중복된 예약은 아닌지 확인한다.
        // 해당 메소드를 지나쳤다는 말은 중복된 예약이 아니라는 것을 의미한다.
        checkIfBookedTwice(member,beforeTime);

        // 특정 강의실, 특정 시간대, 특정 자리에 이미 예약한 좌석이 있는지 확인
        // 해당 메소드는 16시 30분 전에 신청하든, 그 이후에 신청을 하든 우선 자리가 있는지 고려 해야하는 상황이다.
        // 승인에 여부 없이 좌석을 확인해야 한다.
        checkIfSeatAvailable(book,lab);

        // 예약 시작 시간이 16시 30분 전이라면
        Reservation reservation = null;
        if(beforeTime){
            // 조교의 승인 필요 없이 바로 승인이 되어 예약이 이루어진다.
            reservation =reservationRepository.save(book.toApprovedReservation(member,lab));

        }else{
            // book의 roomNum이 우선 순위에 맞는 강의실로 선택했는지 확인한다.
            defaultNewVersionRoom.checkIfRoomIsFull(book);
            // 예약 시작 시간이 16시 30분 이후라면 조교의 승인이 필요하다
            reservation = reservationRepository.save(book.toUnapprovedReservation(member,lab));

            labManagerService.updateLabManager(lab,book);
        }

        // 예약 완료 정보 반환
        return new BookInfo(reservation,lab,member);
    }

    /**
     * 사용자의 모든 예약 내역 가져오기
     * @param userId 사용자 아이디
     */
    @Override
    public ReservationInfos getAllReservationFromMemberId(String userId) {
        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(() -> new BadRequestException("해당 사용자가 존재하지 않습니다."));

        // convert to list of reservation info
      ReservationInfos reservationInfos = new ReservationInfos();
      reservationRepository.findAllByMember(member, Date.valueOf(LocalDate.now()))
          .ifPresent(reservations -> reservationInfos.addReservationInfo(reservations,member));

      return reservationInfos;


    }

    /**
     * 조교는 17시 이후에 사용하고자 하는 미승인된 예약 내역을 조회한다.
     */
    @Override
    public ReservationInfos getUnauthorizedReservation() {
      // 오늘 기점으로 17시 이후에 사용하고자 하는 미승인된 예약 내역을 조회한다.
      Optional<List<Reservation>> unauthorizedReservations =
          reservationRepository.findReservationsByDateAndPermission(Date.valueOf(LocalDate.now()),
              false);

      ReservationInfos reservationInfos = new ReservationInfos();
      unauthorizedReservations.ifPresent(reservations -> reservationInfos.addReservationInfo(reservations));

      return reservationInfos;
    }

    /**
     * 특정 강의실, 특정 시간대에서 승인된 예약 현황(사용자) ,permission이 true인 결과를 조회한다.
     */
    @Override
    public ReservationInfosWithManager getReservationFromRoomNumber(RoomAndTime roomAndTime) {

      // 확인하고자 하는 강의실
      Lab lab = labService.findLabWithRoomNumber(roomAndTime.getRoomNum());

      ReservationInfosWithManager reservationInfosWithManager = new ReservationInfosWithManager();

      reservationRepository.findCurrentReservationWithPermission(lab,roomAndTime.getStartLocalDateTime(),
              roomAndTime.getEndLocalDateTime(),true)
          .ifPresent(reservationInfosWithManager::addReservationInfo);

      // 검색하고자 하는데, 종료 시간이 17시 이후인 경우는 방정 데이터도 함께 response dto에 실어서 보낸다.
      if(roomAndTime.getEndLocalTime().isAfter(LocalTime.of(17,0))){
        MemberSimpleInfo memberSimpleInfo = labManagerService.searchMemberByLabId(lab.getId()); // 방장 기존 정보 반환
        reservationInfosWithManager.setManager(memberSimpleInfo); // 매니저로 지정
      }

      return reservationInfosWithManager;
    }

  /**
     * 팀 인원이 열려 있는 강의실 남은 자리 수 보다 많을 때 예약불가 메세지 알려주기
     */
    private void checkIfTeamSizeIsBiggerThanCapacity(BookRequest book, Lab lab) {
        // 이용하고자 하는 시간 기준으로 특정 강의실에 이용중인 reservation 개수 확인 후 Capacity에서 빼준 값이 이용 가능한 자리 수
        int availableSeatNum = lab.getCapacity() - reservationRepository
            .countByLabAndStartTimeBetweenEndTime(lab, book.getStartTime(), book.getEndTime());

        log.info("availableSeatNum : {}", availableSeatNum);

        // 팀 인원이 현재 강의실 인원보다 많으면 예약 불가
        if(book.getTeamSize() > availableSeatNum){
            log.warn("팀 인원이 현재 강의실 인원보다 많아 예약 불가");
            throw new FullOfCapacityException("예약 가능한 좌석이 부족합니다.");
        }
    }

    /**
     * book의 예약 시작 시간이 16시 반전인지, 이후인지 판단해야 한다.
     */
    private boolean checkIfBookStartBeforeTime(BookRequest book) {
        return book.getStartTime().isBefore(LocalDateTime.of(book.getStartTime().getYear(),book.getStartTime().getMonth(),book.getStartTime().getDayOfMonth(),16,30));
    }

    /**
     * 해당 사용자가 중복된 예약은 없는지 확인
     */
    private void checkIfBookedTwice(Member member, Boolean permission) {

      // 예약 시작 시간이 16시 30분 전이라면
      if(permission){
        reservationRepository.findApprovedReservationByMemberId(member.getId(),true, java.sql.Date.valueOf(LocalDate.now()))
            .map(ReservationInfo::toCurrentReservation)
            .ifPresent(reservationInfo -> {
              log.warn("중복 예약 불가");
              throw new AlreadyBookedException("17전에 이미 예약된 내역이 있습니다. 중복된 예약은 불가합니다.");
            });
      }else{
        reservationRepository.findApprovedReservationByMemberId(member.getId(),false, java.sql.Date.valueOf(LocalDate.now()))
            .map(ReservationInfo::toCurrentReservation)
            .ifPresent(reservationInfo -> {
              log.warn("중복 예약 불가");
              throw new AlreadyBookedException("17시 이후로 이미 예약된 내역이 있습니다. 중복된 예약은 불가합니다.");
            });
      }
    }

    /**
     * 특정 강의실, 특정 시간대, 특정 자리에 이미 예약한 좌석이 있는지 확인
     */
    private void checkIfSeatAvailable(BookRequest book, Lab lab) {

      // 예약 하고자 하는 좌석이 이미 예약된 좌석인지 확인
      reservationRepository
          .findCurrentReservationBetweenTime(lab, book.getStartTime(), book.getEndTime(), Date.valueOf(LocalDate.now()))
          .ifPresent(reservations ->
              { List<String> seatNums = reservations.stream().map(Reservation::getSeatNum)
                    .collect(Collectors.toList());

                  // 예약한 좌석이 있는지 확인
                  if (seatNums.contains(book.getSeatNum())) {
                      log.warn("이미 예약된 좌석입니다.");
                      throw new AlreadyBookedException("이미 예약된 좌석입니다.");
                  }
          });
    }






}
