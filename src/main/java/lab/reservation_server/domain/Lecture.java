package lab.reservation_server.domain;

import java.time.LocalDate;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Lecture {

    /**
     * primary key로 활용되는 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id" , nullable = false, foreignKey = @ForeignKey(name = "fk_lecture_lab"))
    private Lab lab;

    /**
     * 강의의 이름
     */
    @Column(nullable = false)
    private String title;

    /**
     * 강의 담당 교수
     */
    @Column(nullable = false)
    private String professor;

    /**
     * 정규 수업의 경우 개강 날짜
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * 정규 수업의 경우 종강 날짜
     */
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * 강의 시작 시간
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 강의 시작 요일
     */
    @Column(nullable = false)
    private String day;

    /**
     * 강의 종료 시간
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Builder
    public Lecture(Lab lab, String title, String professor, LocalDate startDate, LocalDate endDate, LocalDateTime startTime, LocalDateTime endTime) {
        this.lab = lab;
        this.title = title;
        this.professor = professor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = startTime.getDayOfWeek().toString();
    }





}
