package lab.reservation_server.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lab.reservation_server.domain.Report;
import lab.reservation_server.dto.request.report.ReportRequest;
import lab.reservation_server.dto.response.report.ReportInfo;
import lab.reservation_server.dto.response.report.ReportInfos;
import lab.reservation_server.repository.ReportRepository;
import lab.reservation_server.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    @Transactional
    public String report(ReportRequest report) {

        reportRepository.save(report.toEntity());

        return "문의 및 신고가 접수 되었습니다.";
    }

    @Override
    public ReportInfos getAllReports() {

        List<ReportInfo> reportInfos= reportRepository.findAll().stream()
                .map(ReportInfo::new)
                .collect(Collectors.toList());

        return new ReportInfos(reportInfos);
    }
}
