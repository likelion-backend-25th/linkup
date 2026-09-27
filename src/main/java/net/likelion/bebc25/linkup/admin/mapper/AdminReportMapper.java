package net.likelion.bebc25.linkup.admin.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdminReportMapper {
    List<AdminReportResponse> findReports(AdminReportSearchRequest request);

    AdminReportDetailResponse findReportDetail(@Param("reportId") Long reportId);

    void updateReportStatus(@Param("reportId") Long reportId, @Param("status") String status);

    void hidePost(@Param("postId") Long postId);

    void hideReply(@Param("replyId") Long replyId);

    int increaseWarningCount(@Param("memberId") Long memberId);

    int countPendingReports();

    int countTotalMembers();

    int findWarningCount(@Param("memberId") Long memberId);

    LocalDateTime findWritingRestrictedUntil(@Param("memberId") Long memberId);

    String findMemberStatus(@Param("memberId") Long memberId);

    boolean findPostHidden(@Param("postId") Long postId);

    void restrictWriting(@Param("memberId") Long memberId);

    void suspendMember(@Param("memberId") Long memberId);
}
