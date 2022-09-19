package lab.reservation_server.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    /**
     * primary key로 활용되는 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 토큰 UUID, 6자리
     */
    @Column(unique = true, nullable = false)
    private String value = UUID.randomUUID().toString().substring(0, 6);

    /**
     * 토큰의 유효기간
     */
    @Column(nullable = false)
    private LocalDate expiration;

    @Builder
    public Token(LocalDate expiration) {
        this.expiration = expiration;
    }

}
