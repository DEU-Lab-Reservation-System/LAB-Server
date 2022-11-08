package lab.reservation_server.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * 실습실 사용에 대한 방장을 저장하는 Entity
 */
@Entity
@Getter
@NoArgsConstructor
public class LabManager {

    /**
     * primary key로 활용되는 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 방장의 데이터 저장
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    /**
     * 방장이 관리하는 실습실
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lab lab;


    /**
     * 생성 시간
     */
    @Column(nullable = false)
    private LocalDate createDate;

    //proPost 생성 시점에 createDate를 현재 시간으로 설정
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDate.now();
    }

    public LabManager(Member member, Lab lab) {
        this.member = member;
        this.lab = lab;
    }


    public void updateMember(Member member) {
        this.member = member;
    }
}
