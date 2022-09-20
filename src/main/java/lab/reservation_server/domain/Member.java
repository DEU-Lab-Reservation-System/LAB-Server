package lab.reservation_server.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lab.reservation_server.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTime {

    /**
     * primary key로 활용되는 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 학생이 예약한 내역
     */
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private Reservation reservation;

    /**
     * 학생의 학번
     */
    @Column(unique = true, nullable = false, length = 10)
    private String userId;

    /**
     * 학생의 비밀번호
     */
    @Column(nullable = false)
    private String password;

    /**
     * 학생의 이름
     */
    @Column(nullable = false)
    private String name;

    /**
     * 학생의 이메일 주소
     */
    @Column(nullable = false)
    private String email;

    /**
     * 학생 폰 번호
     */
    @Column(unique = true, nullable = false)
    private Integer phoneNum;

    /**
     * 학생의 권한
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String userId, String password, String name, String email, Integer phoneNum, Role role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.role = role;
    }

}
