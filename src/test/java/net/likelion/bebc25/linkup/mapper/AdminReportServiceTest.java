package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminReportProcessRequest;
import net.likelion.bebc25.linkup.admin.service.AdminReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AdminReportServiceTest {

    @Autowired
    private AdminReportService adminReportService;

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
}
