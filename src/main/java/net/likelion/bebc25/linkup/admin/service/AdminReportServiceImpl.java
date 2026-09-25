package net.likelion.bebc25.linkup.admin.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;
import net.likelion.bebc25.linkup.admin.mapper.AdminReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportServiceImpl implements AdminReportService {
    private final AdminReportMapper adminReportMapper;


    @Override
    public List<AdminReportResponse> getReports(AdminReportSearchRequest request) {
        return adminReportMapper.findReports(request);
    }

    @Override
    public AdminReportDetailResponse getReportDetail(Long reportId) {
        AdminReportDetailResponse response = adminReportMapper.findReportDetail(reportId);

        if(response == null){
            throw new IllegalArgumentException("존재하지 않는 신고입니다.");
        }
        return response;
    }
}
