package net.likelion.bebc25.linkup.admin.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;
import net.likelion.bebc25.linkup.admin.service.AdminReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reports")
public class AdminReporterController {

    private final AdminReportService adminReportService;

    @GetMapping
    public List<AdminReportResponse> getReports(AdminReportSearchRequest request){
        return adminReportService.getReports(request);
    }

    @GetMapping("/{reportId}")
    public AdminReportDetailResponse getReportDetail(@PathVariable("reportId") Long reportId){
        return adminReportService.getReportDetail(reportId);
    }
}
