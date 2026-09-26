package net.likelion.bebc25.linkup.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportProcessRequest;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;
import net.likelion.bebc25.linkup.admin.service.AdminReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{reportId}")
    public ResponseEntity<Void> processReport(
            @PathVariable Long reportId, @Valid @RequestBody AdminReportProcessRequest request
    ){
        adminReportService.processReport(reportId, request);

        return ResponseEntity.ok().build();
    }

}
