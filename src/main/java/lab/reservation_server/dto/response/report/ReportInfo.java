package lab.reservation_server.dto.response.report;

import lab.reservation_server.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 문의 및 신고 내용을 response dto
 */
@Getter
@AllArgsConstructor
public class ReportInfo {

    private Long id;
    private String title;
    private String content;
    private String writerName;
    private String userId;
    private String createDate;

    public ReportInfo(Report report) {
        this.id = report.getId();
        this.title = report.getTitle();
        this.content = report.getContent();
        this.writerName = report.getWriterName();
        this.userId = report.getUserId();
        this.createDate = report.getCreateDate().toString();
    }

}
