package lab.reservation_server.service;

import lab.reservation_server.dto.request.report.ReportRequest;

/**
 * 문의 및 신고
 */
public interface ReportService {
  String report(ReportRequest report);
}
