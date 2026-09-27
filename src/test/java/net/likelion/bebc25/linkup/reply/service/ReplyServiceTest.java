package net.likelion.bebc25.linkup.reply.service;

import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.reply.domain.Reply;
import net.likelion.bebc25.linkup.reply.dto.ReplyCreateRequest;
import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyUpdateRequest;
import net.likelion.bebc25.linkup.reply.mapper.ReplyMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ReplyServiceTest {
    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyMapper replyMapper;

    @Test
    @DisplayName("댓글 마지막 페이지 조회 테스트")
    void getReplyLastPageTest() {
        // when
        ReplyPageResponse response =
                replyService.getReplies(1L, 2L, null, 3);

        // then
        assertThat(response.replies())
                .extracting(ReplyResponse::id)
                .containsExactly(1L);

        assertThat(response.hasNext()).isFalse();
        assertThat(response.nextCursor()).isNull();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createReplyTest() {
        // given
        ReplyCreateRequest createRequest = new ReplyCreateRequest("테스트 댓글");

        // when
        Long replyId = replyService.createReply(1L, 2L, createRequest);

        // then
        Reply result = replyMapper.getById(replyId);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(replyId);
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getMemberId()).isEqualTo(2L);
        assertThat(result.getContent()).isEqualTo("테스트 댓글");
    }

    @Test
    @DisplayName("댓글 삭제 테스트 | 1. 댓글 작성자인 경우")
    void deleteReplyTest1() {
        // given
        Long replyId = createReply(1L, 2L, "댓글 작성자 삭제 테스트");

        // when
        replyService.deleteReply(1L, 2L, replyId);

        // then
        assertThat(replyMapper.getById(replyId)).isNull();
    }

    @Test
    @DisplayName("댓글 삭제 테스트 | 2. 댓글 작성자가 아닌 경우")
    void deleteReplyTest2() {
        // given
        Long replyId = createReply(1L, 2L, "댓글 작성자가 아닌 삭제 테스트");

        // when
        assertThatThrownBy(() ->
                replyService.deleteReply(1L, 3L, replyId)
        )
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        AssertionsForClassTypes.assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.FORBIDDEN)
                );

        // then
        assertThat(replyMapper.getById(replyId)).isNotNull();
    }

    @Test
    @DisplayName("댓글 삭제 테스트 | 3. 게시글 작성자인 경우")
    void deleteReplyTest3() {
        // given
        Long replyId = createReply(1L, 2L, "게시글 작성자가 삭제 테스트");

        // when
        replyService.deleteReply(1L, 1L, replyId);

        // then
        assertThat(replyMapper.getById(replyId)).isNull();
    }

    @Test
    @DisplayName("댓글 수정 테스트 | 1. 댓글 작성자인 경우")
    void updateReplyTest1() {
        // given
        Long replyId = createReply(1L, 2L, "테스트 댓글");

        // when
        replyService.updateReply(1L, 2L, replyId, new ReplyUpdateRequest("수정된 댓글"));

        // then
        assertThat(replyMapper.getById(replyId).getContent()).isEqualTo("수정된 댓글");
    }

    @Test
    @DisplayName("댓글 수정 테스트 | 2. 댓글 작성자가 아닌 경우")
    void updateReplyTest2() {
        // given
        Long replyId = createReply(1L, 2L, "테스트 댓글");

        // when
        assertThatThrownBy(() ->
                replyService.updateReply(1L, 3L, replyId, new ReplyUpdateRequest("수정된 댓글"))
        )
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        AssertionsForClassTypes.assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.FORBIDDEN)
                );


        // then
        assertThat(replyMapper.getById(replyId).getContent()).isNotEqualTo("수정된 댓글");
    }

    Long createReply(Long postId, Long memberId, String content) {
        Reply reply = Reply.builder()
                .postId(postId)
                .memberId(memberId)
                .content(content)
                .build();

        replyMapper.insert(reply);
        return reply.getId();
    }
}
