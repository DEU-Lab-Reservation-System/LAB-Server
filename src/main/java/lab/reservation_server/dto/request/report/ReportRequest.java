package lab.reservation_server.dto.request.report;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lab.reservation_server.domain.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문의 및 신고
 */
@Getter
@NoArgsConstructor
public class ReportRequest {

    @NotBlank(message = "제목을 입력해주세요")
    @ApiModelProperty(value = "문의 및 신고에서의 제목")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @ApiModelProperty(value = "문의 및 신고에서의 내용")
    private String content;

    @NotBlank(message = "작성자 이름을 작성해주세요")
    @ApiModelProperty(value = "문의 및 신고에서의 작성자")
    private String writerName;

    @NotBlank(message = "작성자의 학번을 입력해주세요")
    @ApiModelProperty(value = "문의 및 신고에서의 작성자의 학번(아이디)")
    private String userId;

    public Report toEntity() {
        return Report.builder()
            .title(title)
            .content(content)
            .writerName(writerName)
            .userId(userId)
            .build();


    }
}
