package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.report.dto.ReportCreateRequest;
import net.likelion.bebc25.linkup.report.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class ReportServiceTest {
    @Autowired
    private ReportService reportService;

    @Test
    @DisplayName("게시글 신고 등록 테스트")
    void createPostReportTest(){
        //given
        Long memberId = 2L;
        Long postId = 1L;
        ReportCreateRequest request = new ReportCreateRequest(
                "부적절한 콘텐츠", "게시글 내용이 신고 대상에 해당합니다.");

        //when
        reportService.createdPostReport(memberId, postId, request);

        // then
        // 정상적으로 등록되면 예외 발생 X
    }

    @Test
    @DisplayName("본인 게시글 신고 테스트")
    void reportOwnPostTest(){
        // given
        Long memberId = 1L;
        Long postId = 1L;
        ReportCreateRequest request = new ReportCreateRequest(
                "부적절한 콘텐츠", "본인 게시글 신고 테스트");

        // when & then
        assertThatThrownBy(() ->
                reportService.createdPostReport(
                        memberId,
                        postId,
                        request
                )
        )       .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 게시글은 신고할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 신고 테스트")
    void reportNotExistPostReportTest() {
        // given
        Long memberId = 1L;
        Long postId = 99999L;
        ReportCreateRequest request = new ReportCreateRequest(
                "부적절한 콘텐츠", "존재하지 않는 게시글 신고 테스트");

        // when & then
        assertThatThrownBy(() ->
                reportService.createdPostReport(
                        memberId,
                        postId,
                        request
                )
        )       .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 게시글입니다.");
    }
}
