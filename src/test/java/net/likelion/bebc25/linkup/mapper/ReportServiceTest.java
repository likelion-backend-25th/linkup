package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.post.service.PostService;
import net.likelion.bebc25.linkup.reply.domain.Reply;
import net.likelion.bebc25.linkup.reply.mapper.ReplyMapper;
import net.likelion.bebc25.linkup.report.dto.ReportCreateRequest;
import net.likelion.bebc25.linkup.report.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class ReportServiceTest {
    @Autowired
    private ReportService reportService;

    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private PostService postService;

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


    @Test
    @DisplayName("댓글 신고 등록 테스트")
    void createReplyReportTest(){
        // given
        Long replyId = 1L;
        Reply reply = replyMapper.getById(replyId);

        Long memberId = 1L;
        Long postId = reply.getPostId();

        ReportCreateRequest request = new ReportCreateRequest("부적절한 콘텐츠", "댓글 내용이 신고 대상에 해당합니다.");

        // when
        reportService.createdReplyReport(memberId, postId, replyId, request);
    }


    @Test
    @DisplayName("본인 댓글 신고 테스트")
    void reportOwnReplyTest(){
        // given
        Long replyId = 1L;
        Reply reply = replyMapper.getById(replyId);

        Long memberId = reply.getMemberId();
        Long postId = reply.getPostId();

        System.out.println("replyId = " + replyId);
        System.out.println("reply memberId = " + reply.getMemberId());
        System.out.println("test memberId = " + memberId);
        System.out.println("postId = " + postId);

        ReportCreateRequest request = new ReportCreateRequest("부적절한 콘텐츠", "본인 댓글 신고 테스트");

        // when & then
        assertThatThrownBy(() ->
                reportService.createdReplyReport(
                        memberId,
                        postId,
                        replyId,
                        request)
        )       .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 댓글은 신고할 수 없습니다.");
    }


    @Test
    @DisplayName("존재하지 않는 댓글 신고 테스트")
    void reportNotExistReplyTest(){
        // given
        Long memberId = 1L;
        Long postId = 1L;
        Long replyId = 99999L;

        ReportCreateRequest request = new ReportCreateRequest("부적절한 콘텐츠", "존재하지 않는 댓글 신고 테스트");

        // when & then
        assertThatThrownBy(() ->
                reportService.createdReplyReport(
                        memberId,
                        postId,
                        replyId,
                        request
                )
        )       .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }


    @Test
    @DisplayName("다른 게시글 댓글 신고 테스트")
    void reportReplyWithWrongPostTest(){
        // given
        Long replyId = 1L;
        Reply reply = replyMapper.getById(replyId);

        Long memberId = 1L;
        Long wrongPostId = reply.getPostId() + 99999L;

        ReportCreateRequest request = new ReportCreateRequest("부적절한 콘텐츠", "다른 게시글의 댓글 신고 테스트");

        // when & then
        assertThatThrownBy(() ->
                reportService.createdReplyReport(
                        memberId,
                        wrongPostId,
                        replyId,
                        request
                )
        )       .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 댓글입니다.");

    }


}
