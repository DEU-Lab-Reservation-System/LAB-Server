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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_reservation_member"))
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



}
