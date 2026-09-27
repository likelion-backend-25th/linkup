package net.likelion.bebc25.linkup.admin.service;

import net.likelion.bebc25.linkup.admin.dto.*;

import java.util.List;

public interface AdminReportService {
    List<AdminReportResponse> getReports(AdminReportSearchRequest request);

    AdminReportDetailResponse getReportDetail(Long reportId);

    void processReport(Long reportId, AdminReportProcessRequest request);

    AdminOperationResponse getOperation();
}
