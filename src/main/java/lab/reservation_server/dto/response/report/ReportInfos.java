package lab.reservation_server.dto.response.report;


import java.util.List;
import lombok.Getter;

@Getter
public class ReportInfos {

    private List<ReportInfo> reports;


    public ReportInfos(List<ReportInfo> reports) {
        this.reports = reports;
    }


}
