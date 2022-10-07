package lab.reservation_server.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
     * 2022-10-08 해당 요일에 오전, 오후에 예약하고 사용한 내역을 DB에 저장하고 있기 때문에
     * OneToOne -> OneToMany로 변경
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

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
    private String phoneNum;

    /**
     * 학생의 권한
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * 학생 휴대폰의 디바이스 토큰
     * 10-01, 회원 가입 시점에는 null이 가능하도록 변경
     */
    @Column(nullable = true)
    private String deviceToken;

    /**
     * 학생 인증 여부
     */
    @Column(nullable = false)
    private Boolean isAuth;

    @Builder
    public Member(String userId, String password, String name, String email, String phoneNum, Role role, String deviceToken, Boolean isAuth) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.role = role;
        this.deviceToken = deviceToken;
        this.isAuth = isAuth;
    }

    /**
     * 토큰 인증이 완료되면 isAuth 필드 true로 변경
     */
    public void updateAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
