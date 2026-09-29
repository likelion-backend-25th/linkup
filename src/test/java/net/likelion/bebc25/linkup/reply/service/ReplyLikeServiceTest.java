package net.likelion.bebc25.linkup.reply.service;

import net.likelion.bebc25.linkup.reply.domain.Reply;
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
public class ReplyLikeServiceTest {
    @Autowired
    ReplyLikeService replyLikeService;
    @Autowired
    ReplyMapper replyMapper;

    @Test
    @DisplayName("댓글 좋아요 등록 테스트 | 1. 정상적인 좋아요 등록")
    void insertReplyLikeTest1() {
        // given
        Long replyId = createReply(1L, 2L, "테스트 댓글");

        // when
        replyLikeService.likeReply(1L, replyId, 2L);
        replyLikeService.likeReply(1L, replyId, 3L);

        // then
        assertThat(replyMapper.getById(replyId).getLikeCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글 좋아요 등록 테스트 | 2. 중복된 좋아요 등록")
    void insertReplyLikeTest2() {
        // given
        Long replyId = createReply(1L, 2L, "테스트 댓글");

        // when
        replyLikeService.likeReply(1L, replyId, 2L);
        replyLikeService.likeReply(1L, replyId, 3L);

        // then
        assertThatThrownBy(() ->
                replyLikeService.likeReply(1L, replyId, 2L)
        )
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        AssertionsForClassTypes.assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.CONFLICT)
                );

        assertThat(replyMapper.getById(replyId).getLikeCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 테스트 | 1. 정상적인 좋아요 삭제")
    void deleteReplyLikeTest1() {
        // given
        Long replyId = createReply(1L, 2L, "테스트 댓글");

        // when
        replyLikeService.likeReply(1L, replyId, 2L);
        replyLikeService.likeReply(1L, replyId, 3L);
        replyLikeService.unlikeReply(1L, replyId, 2L);

        // then
        assertThat(replyMapper.getById(replyId).getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 테스트 | 2. 존재하지 않는 좋아요 삭제")
    void deleteReplyLikeTest2() {
        // given
        Long replyId = createReply(1L, 2L, "테스트 댓글");

        // when
        replyLikeService.likeReply(1L, replyId, 2L);
        replyLikeService.likeReply(1L, replyId, 3L);
        replyLikeService.unlikeReply(1L, replyId, 4L);

        // then
        assertThat(replyMapper.getById(replyId).getLikeCount()).isEqualTo(2);
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
