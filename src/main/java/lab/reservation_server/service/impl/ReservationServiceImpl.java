package lab.reservation_server.service.impl;


import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Lab;
import lab.reservation_server.domain.Member;
import lab.reservation_server.domain.Reservation;
import lab.reservation_server.dto.request.reservation.BookRequest;
import lab.reservation_server.dto.request.reservation.TimeStartToEnd;
import lab.reservation_server.dto.response.labmanager.MemberSimpleInfo;
import lab.reservation_server.dto.response.reservation.BookInfo;
import lab.reservation_server.dto.response.reservation.CurrentReservation;
import lab.reservation_server.dto.response.reservation.ReservationInfo;
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
      public ReservationInfo getReservationFromMemberId(Long memberId) {

        // 값이 있으면, reservationInfo를 만들어서 반환, 없으면 null 반환
        // find first of list and conver to ReservationInfo
        ReservationInfo reservationInfo =
            reservationRepository.findReservationByMemberId(memberId).map(ReservationInfo::toCurrentReservation)
                .orElse(null);

        return reservationInfo;
      }

      /**
       * 현재 시간 기준으로 해당 강의실에 강의가 있는지 확인, 없으면 현 사용중인 좌석 반환
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

        return new CurrentReservation(seatNums, memberSimpleInfo);
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

      return new CurrentReservation(seatNums, memberSimpleInfo);
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

        // todo : 예약하고자 하는 시간에서 수업이 있는지 없는지 판단해야 한다. 이미 검증을 했지만 최종적으로 한번더 판단하고자 한다. -> OK
        // todo : if 분기를 통해 원래 17시 이후에는 원래 수업이 없지만 혹시나 모를 수업에 대비해서 수업이 있는지 검증을 한다. -> OK
        // 실습실 정보, 예약 시작 시간, 예약 종료 시간, 예약하고자 하는 날짜를 통해서 판단해야 한다.
        lectureService.checkLectureBetweenTime(lab, book.getStartTime().toLocalTime(), book.getEndTime().toLocalTime());

        // 팀 인원이 현재 강의실 인원보다 많으면 예약 불가
        checkIfTeamSizeIsBiggerThanCapacity(book,lab);

      // 해당 사용자가 중복된 예약은 아닌지 확인한다.
      // 해당 메소드를 지나쳤다는 말은 중복된 예약이 아니라는 것을 의미한다.
      checkIfBookedTwice(member,beforeTime);

      // 특정 강의실, 특정 시간대, 특정 자리에 이미 예약한 좌석이 있는지 확인
      // 해당 메소드는 16시 30분 전에 신청하든, 그 이후에 신청을 하든 우선 자리가 있는지 고려 해야하는 상황이다.
      // 승인에 여부 없이 좌석을 확인해야 한다. 16시 반 전에 신청한 경우면 true
      checkIfSeatAvailable(book,lab);


        // 예약 시작 시간이 16시 30분 전이라면
        Reservation reservation = null;
        if(beforeTime){
            // 조교의 승인 필요 없이 바로 승인이 되어 예약이 이루어진다.
            reservation =reservationRepository.save(book.toApprovedReservation(member,lab));
        }else{
            // 예약 시작 시간이 16시 30분 이후라면
            // 조교의 승인이 필요하다.

            // book의 roomNum이 우선 911인지 확인한다.
            // 예약 목록 false -> 911-> 915,916,918 순서대로 사람이 들어가야 한다.
            defaultNewVersionRoom.checkIfRoomIsFull(book);

            reservation = reservationRepository.save(book.toUnapprovedReservation(member,lab));








            // todo : save 하는 과정에서 17시 이후에 대해서는 먼저 우선순위가 높은 강의실부터 열려야 한다.
            // todo : 17시 이후의 예약은 언제든지 먼저 예약을 잡을 수 있다.
            // todo : 따라서 순차적으로 17시 이후의 예약을 잡게 되면, 그 목록을 순차적으로 유지하면서 순회를 해야 한다.
            // todo : 각각을 순회를 하면서 17 이후의 경우에 대해서 911 강의실을 먼저 요청하도록 한다.




        }

        // todo : 예약 완료 후, 방장 업데이트 필요
        // 현재 예약 목록에서 방장이 없다면 방장을 업데이트 한다.
        // 현재 시간기점으로 이용중인 사용자 중에서 가장 오랫동안 있는 사람...

        // todo : 방장에 대해서는 17시 전, 후로 가장 마지막으로 이용하는 사용자에 대해서만 방장 권한을 부여하면 된다.

        // todo : 방장을 업데이트 하고 나서는 반드시 해당 강의실 이용자에게 알림을 보내야 한다.

        // 예약 완료 정보 반환
        return new BookInfo(reservation,lab,member);
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
        reservationRepository.findApprovedReservationByMemberId(member.getId(),true).map(ReservationInfo::toCurrentReservation)
            .ifPresent(reservationInfo -> {
              log.warn("중복 예약 불가");
              throw new AlreadyBookedException("17전에 이미 예약된 내역이 있습니다. 중복된 예약은 불가합니다.");
            });
      }else{
        reservationRepository.findApprovedReservationByMemberId(member.getId(),false).map(ReservationInfo::toCurrentReservation)
            .ifPresent(reservationInfo -> {
              log.warn("중복 예약 불가");
              throw new AlreadyBookedException("17시 이후로 이미 예약된 내역이 있습니다. 중복된 예약은 불가합니다.");
            });
      }



      // 해당 사용자가 중복된 예약은 아닌지 확인한다.
      // 예약한 적이 없거나, 예약한 적이 있지만, 예약한 시간이 지났다면 null로 나와서 예약 가능
      // 하지만 예약한 적이 있고, 예약한 시간이 지나지 않았다면 예외를 발생시킨다. (중복 예약 불가)
//      reservationRepository.findReservationByMemberId(member.getId()).map(ReservationInfo::toCurrentReservation)
//            .ifPresent(reservationInfo -> {
//                log.warn("중복 예약 불가");
//                throw new AlreadyBookedException("이미 예약된 내역이 있습니다. 중복된 예약은 불가합니다.");
//            });
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
