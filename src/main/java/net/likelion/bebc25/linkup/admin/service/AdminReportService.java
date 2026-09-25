package net.likelion.bebc25.linkup.admin.service;

import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;

import java.util.List;

public interface AdminReportService {
    List<AdminReportResponse> getReports(AdminReportSearchRequest request);

    AdminReportDetailResponse getReportDetail(Long reportId);
}
