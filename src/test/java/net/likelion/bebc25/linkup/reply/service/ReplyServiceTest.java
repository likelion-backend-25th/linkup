package net.likelion.bebc25.linkup.reply.service;

import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReplyServiceTest {
    @Autowired
    ReplyService replyService;

    @Test
    @DisplayName("댓글 마지막 페이지 조회 테스트")
    void getReplyLastPage() {
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
}
