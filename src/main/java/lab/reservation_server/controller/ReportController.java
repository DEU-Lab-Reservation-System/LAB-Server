package lab.reservation_server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lab.reservation_server.dto.request.report.ReportRequest;
import lab.reservation_server.dto.response.DefaultMessageResponse;
import lab.reservation_server.dto.response.report.ReportInfos;
import lab.reservation_server.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "ReportController : 문의 및 신고")
@Slf4j
public class ReportController {

    private final ReportService reportService;


    /**
     * 문의 및 신고를 할 수 있다.
     */
    @PostMapping("/api/report")
    @ApiOperation(value="문의 및 신고" , notes = "문의 및 신고를 할 수 있다.")
    public ResponseEntity<DefaultMessageResponse> report(@RequestBody @Valid ReportRequest report) {

        String message = reportService.report(report);

        return ResponseEntity.ok().body(new DefaultMessageResponse(message));
    }

    /**
     * 교수는 문의 신고 내역을 조회할 수 있다.
     */
    @GetMapping("/api/report")
    @ApiOperation(value="문의 신고 내역 조회" , notes = "교수는 문의 신고 내역을 조회할 수 있다.")
    public ResponseEntity<ReportInfos> getReport() {

        ReportInfos reportInfos = reportService.getAllReports();

        return ResponseEntity.ok(reportInfos);
    }







}
