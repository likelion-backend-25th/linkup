package net.likelion.bebc25.linkup.admin.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.admin.dto.*;
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

        // 신고 상태 변경
        adminReportMapper.updateReportStatus(reportId, status);

        // 신고 기각
        if("REJECTED".equals(status)){
            return;
        }

        // 콘텐츠 삭제 처리
        if("POST".equals(report.targetType())){
            adminReportMapper.deletePost(report.postId());
        } else if("REPLY".equals(report.targetType())){
            adminReportMapper.deleteReply(report.replyId());
        }

        // 대상 회원 경고 증가
        adminReportMapper.increaseWarningCount(report.targetMemberId());

        // 증가된 경고 수 확인
        int warningCount = adminReportMapper.findWarningCount(report.targetMemberId());

        // 게시글 작성 제한
        if(warningCount == 2){
            adminReportMapper.restrictWriting(report.targetMemberId());
            // 회원 정지
        } else if(warningCount >= 3) {
            adminReportMapper.suspendMember(report.targetMemberId());
        }
    }

    @Override
    public AdminOperationResponse getOperation() {
        int pendingReportCount = adminReportMapper.countPendingReports();
        int totalMemberCount = adminReportMapper.countTotalMembers();

        return new AdminOperationResponse(pendingReportCount, totalMemberCount);
    }
}
