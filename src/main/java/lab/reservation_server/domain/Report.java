package lab.reservation_server.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문의 신고
 */
@Entity
@Getter
@NoArgsConstructor
public class Report {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writerName;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime createDate;


    @PrePersist
    public void prePersist() {
       this.createDate = LocalDateTime.now();
    }

    @Builder
    public Report(String title, String content, String writerName, String userId) {
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.userId = userId;
    }


}
