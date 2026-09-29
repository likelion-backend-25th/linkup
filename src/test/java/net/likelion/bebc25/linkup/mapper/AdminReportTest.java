package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;
import net.likelion.bebc25.linkup.admin.service.AdminReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class AdminReportTest {

    @Autowired
    private AdminReportService adminReportService;

    @Test
    @DisplayName("신고 목록 전체 조회 테스트")
    void findReports(){
        //given
        AdminReportSearchRequest request = new AdminReportSearchRequest();
        request.setPage(1);
        request.setSize(10);

        // when
        List<AdminReportResponse> reports = adminReportService.getReports(request);

        // then
            assertThat(reports).isNotNull();
            assertThat(reports).isNotEmpty();
            for(AdminReportResponse report : reports){
                assertThat(report.id()).isNotNull();
                assertThat(report.targetType()).isNotNull();
                assertThat(report.reporterName()).isNotNull();
                assertThat(report.reason()).isNotNull();
                assertThat(report.createdAt()).isNotNull();
                assertThat(report.status()).isNotNull();
            }
        }

    @Test
    @DisplayName("게시글 신고 유형별 조회 테스트")
    void findPostReportsTest(){
        //given
        AdminReportSearchRequest request = new AdminReportSearchRequest();
        request.setTargetType("POST");;
        request.setPage(1);
        request.setSize(10);

        // when
        List<AdminReportResponse> reports = adminReportService.getReports(request);

        // then
        for(AdminReportResponse report : reports){
            assertThat(report.targetType()).isEqualTo("POST");
        }
    }

    @Test
    @DisplayName("대기 상태 신고 목록 조회 테스트")
    void findWaitingReportsTest(){
        // given
        AdminReportSearchRequest request = new AdminReportSearchRequest();
        request.setStatus("WAIT");
        request.setPage(1);
        request.setSize(10);

        // when
        List<AdminReportResponse> reports = adminReportService.getReports(request);

        // then
        assertThat(reports).isNotNull();
        for(AdminReportResponse report : reports){
            assertThat(report.status()).isEqualTo("WAIT");
        }
    }

    @Test
    @DisplayName("신고 상세 조회 테스트")
    void findReportDetailTest(){
        // given
        AdminReportSearchRequest request = new AdminReportSearchRequest();
        // 댓글 기능 미구현으로 임시 코드 추가
        // 구현 완료 후 삭제
        request.setTargetType("POST");

        request.setPage(1);
        request.setSize(10);

        List<AdminReportResponse> reports = adminReportService.getReports(request);

        assertThat(reports).isNotEmpty();

        Long reportId = reports.getFirst().id();

        // when
        AdminReportDetailResponse detail = adminReportService.getReportDetail(reportId);

        System.out.println("detail = " + detail);

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.id()).isEqualTo(reportId);
//        assertThat(detail.targetType()).isNotNull();
        // 댓글 기능 미구현으로 게시글만 테스트
        // 댓글 기능 구현 완료되면 아래 코드 삭제 후 위 주석 코드 활성화
            assertThat(detail.targetType()).isEqualTo("POST");
        assertThat(detail.reporterName()).isNotNull();
        assertThat(detail.targetUserName()).isNotNull();
        assertThat(detail.content()).isNotNull();
        assertThat(detail.reason()).isNotNull();
        assertThat(detail.reportContent()).isNotNull();
        assertThat(detail.createdAt()).isNotNull();
        assertThat(detail.status()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 신고 상세 조회 시 예외 발생 테스트")
    void findReportDetailWithInvalidTest(){
        Long invalidReportId = 99999L;

        // when & then
        assertThatThrownBy(() ->
                adminReportService.getReportDetail(invalidReportId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 신고입니다.");
    }
}