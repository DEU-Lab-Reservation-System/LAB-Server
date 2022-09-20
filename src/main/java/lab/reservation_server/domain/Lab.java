package lab.reservation_server.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Lab {

    /**
     * primary key로 활용되는 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 실습실에 열리는 강의 목록
     */
    @OneToMany(mappedBy = "lab", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Lecture> lectures;

    /**
     * 실습실 호수 (방번호)
     */
    @Column(unique = true, nullable = false)
    private String number;

    /**
     * 살숩실의 수용 가능 인원
     */
    @Column(nullable = false)
    private Integer capacity;

    @Builder
    public Lab(String number, Integer capacity) {
        this.number = number;
        this.capacity = capacity;
    }

}
