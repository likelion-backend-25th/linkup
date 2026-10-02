package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminOperationResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportProcessRequest;
import net.likelion.bebc25.linkup.admin.mapper.AdminReportMapper;
import net.likelion.bebc25.linkup.admin.service.AdminReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AdminReportServiceTest {

    @Autowired
    private AdminReportService adminReportService;
    @Autowired
    private AdminReportMapper adminReportMapper;

    @Test
    @DisplayName("신고 기각 테스트")
    void rejectReportTest(){
        //given
        Long reportId = 1L;
        AdminReportProcessRequest request = new AdminReportProcessRequest("REJECTED");

        // when
        adminReportService.processReport(reportId,request);

        //then
        assertThat(request.status()).isEqualTo("REJECTED");
    }

    @Test
    @DisplayName("신고 처리 완료 테스트")
    void resolveReportTest(){
        // given
        Long reportId = 2L;
        AdminReportProcessRequest request = new AdminReportProcessRequest("RESOLVED");

        // when
        adminReportService.processReport(reportId, request);

        // then
        assertThat(request.status()).isEqualTo("RESOLVED");
    }

    @Test
    @DisplayName("존재하지 않는 신고 처리 테스트")
    void processNoneReportTest(){
        // given
        Long reportId = 99999L;
        AdminReportProcessRequest request = new AdminReportProcessRequest("REJECTED");

        // when & then
        assertThatThrownBy(() ->
                adminReportService.processReport(reportId, request)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 신고입니다.");
    }

    @Test
    @DisplayName("처리 완료된 신고 처리 테스트")
    void processAlreadyTest(){
        //given
        Long reportId = 20L;
        AdminReportProcessRequest request = new AdminReportProcessRequest("RESOLVED");

        // when & then
        assertThatThrownBy(() ->
                adminReportService.processReport(reportId, request)
                )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 처리된 신고입니다.");
    }


    @Test
    @DisplayName("잘못된 신고 처리 상태 사용 불가")
    void processInvalidTest(){
        // given
        Long reportId = 1L;
        AdminReportProcessRequest request = new AdminReportProcessRequest("INVALID");

        // when & then
        assertThatThrownBy(() ->
                adminReportService.processReport(reportId, request)
                )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 신고 처리 상태입니다.");
    }

    @Test
    @DisplayName("신고 처리 시 대상 회원의 경고 횟수 증가")
    void increaseWarningCountTest(){
        // given
        Long reportId = 1L;
        AdminReportDetailResponse report = adminReportService.getReportDetail(reportId);
        int beforeWarningCount = adminReportMapper.findWarningCount(report.targetMemberId());
        AdminReportProcessRequest request = new AdminReportProcessRequest("RESOLVED");

        // when
        adminReportService.processReport(reportId, request);

        // then
        int afterWarningCount = adminReportMapper.findWarningCount(report.targetMemberId());
        assertThat(afterWarningCount).isEqualTo(beforeWarningCount + 1);
    }

    @Test
    @DisplayName("경고 2회 누적 시 게시글 작성 7일 제한 테스트")
            void secondWarningTest(){
        // given
        Long firstReportId = 1L;
        Long secondReportId = 2L;
        AdminReportProcessRequest request = new AdminReportProcessRequest("RESOLVED");

        //when
        adminReportService.processReport(firstReportId, request);
        adminReportService.processReport(secondReportId, request);

        // then
        AdminReportDetailResponse secondReport = adminReportService.getReportDetail(secondReportId);
        Integer warningCount = adminReportMapper.findWarningCount(secondReport.targetMemberId());
        LocalDateTime restrictedUntil = adminReportMapper.findWritingRestrictedUntil(secondReport.targetMemberId());

        assertThat(warningCount).isEqualTo(2);
        assertThat(restrictedUntil).isNotNull();
        assertThat(restrictedUntil).isAfter(LocalDateTime.now());
    }

    @Test
    @DisplayName("경고 3회 누적 시 회원 정지 테스트")
    void thirdWarningTest(){
        // given
        Long firstReportId = 1L;
        Long secondReportId = 2L;
        Long thirdReportId = 4L;

        AdminReportProcessRequest request = new AdminReportProcessRequest("RESOLVED");

        // when
        adminReportService.processReport(firstReportId, request);
        adminReportService.processReport(secondReportId, request);
        adminReportService.processReport(thirdReportId, request);

        //then
        AdminReportDetailResponse thirdReport = adminReportService.getReportDetail(thirdReportId);
        int warningCount = adminReportMapper.findWarningCount(thirdReport.targetMemberId());
        String memberStatus = adminReportMapper.findMemberStatus(thirdReport.targetMemberId());

        assertThat(warningCount).isEqualTo(3);
        assertThat(memberStatus).isEqualTo("SUSPENDED");
    }

    @Test
    @DisplayName("신고 처리 완료 시 게시글 숨김 처리 테스트")
    void hidePostAfterResolveTest(){
        // given
        Long reportId = 1L;
        AdminReportDetailResponse report = adminReportService.getReportDetail(reportId);

        Long postId = report.postId();
        AdminReportProcessRequest request = new AdminReportProcessRequest("RESOLVED");

        // when
        adminReportService.processReport(reportId, request);

    }


    @Test
    @DisplayName("관리자 운영 현황 조회")
    void getOperationTest() {
        // given
        int exceptedPendingReportCount = adminReportMapper.countPendingReports();
        int exceptedTotalMemberCount = adminReportMapper.countTotalMembers();

        // when
        AdminOperationResponse response = adminReportService.getOperation();

        // then
        assertThat(response).isNotNull();
        assertThat(response.pendingReportCount()).isEqualTo(exceptedPendingReportCount);
        assertThat(response.totalMember()).isEqualTo(exceptedTotalMemberCount);
    }
}
