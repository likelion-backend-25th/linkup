package net.likelion.bebc25.linkup.admin.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportProcessRequest;
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

    @Override
    @Transactional
    public void processReport(Long reportId, AdminReportProcessRequest request){
        AdminReportDetailResponse report = adminReportMapper.findReportDetail(reportId);

        if(report == null) {
            throw new IllegalArgumentException("존재하지 않는 신고입니다.");
        }

        if(!"WAIT".equals(report.status())){
            throw new IllegalArgumentException("이미 처리된 신고입니다.");
        }

        String status = request.status();

        if(!"REJECTED".equals(status) && !"RESOLVED".equals(status)){
            throw new IllegalArgumentException("올바르지 않은 신고 처리 상태입니다.");
        }

        adminReportMapper.updateReportStatus(reportId, status);

        // 신고 기각
        if("REJECTED".equals(status)){
            return;
        }

        // 신고 처리 완료
        if("POST".equals(report.targetType())){
            adminReportMapper.hidePost(report.postId());
        } else if("REPLY".equals(report.targetType())){
            adminReportMapper.hideReply(report.replyId());
        }

        adminReportMapper.increaseWarningCount(report.targetMemberId());
    }
}
