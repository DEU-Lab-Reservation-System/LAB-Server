package lab.reservation_server.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@NoArgsConstructor
public class Reservation extends BaseTime {

    /**
     * primary key로 활용되는 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 2022-10-08
     * Reservation 입장에서 오늘 예약한 Member가 2건 이상일 수 있기 때문에,
     * OneToOne에서 ManyToOne으로 변경 (2건일 수 있는 경우는 예약이 종료되면, 바로 Reservation DB 데이터를
     * 날리지 않기 때문이다. 추후 스케줄링을 통해서 사용자가 사용하지 않는 시간대에 삭제하기 위해)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id" , nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_lab"))
    private Lab lab;

    /**
     * 예약한 좌석
     */
    @Column(nullable = false)
    private String seatNum;

    /**
     * 이용 시작 시간
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 이용 종료 시간(만료시간)
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 연장 가능한 시간
     */
    @Column(nullable = false)
    private LocalDateTime extensionTime;

    /**
     * 예약 승인 여부(조교로부터)
     */
    @Column(nullable = false)
    private Boolean permission;


    @Builder
    public Reservation(Member member, Lab lab, String seatNum, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime extensionTime, Boolean permission) {
        this.member = member;
        this.lab = lab;
        this.seatNum = seatNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.extensionTime = extensionTime;
        this.permission = permission;
    }


    public void updateEndTime(LocalDateTime extendedEndTime) {
        this.endTime = extendedEndTime;
        this.extensionTime = extendedEndTime.minusMinutes(30);
    }
}
