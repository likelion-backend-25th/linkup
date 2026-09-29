package net.likelion.bebc25.linkup.reply.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class ReplyLikeMapperTest {
    @Autowired
    private ReplyLikeMapper replyLikeMapper;

    @Test
    @DisplayName("댓글 좋아요 등록 테스트")
    void insertReplyLikeTest() {
        // given


        // when
        int result = replyLikeMapper.insert(1L, 2L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(replyLikeMapper.existsByReplyIdAndMemberId(1L, 2L)).isNotNull();
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 테스트")
    void deleteReplyLikeTest() {
        // given
        replyLikeMapper.insert(1L, 2L);
        replyLikeMapper.insert(1L, 3L);

        // when
        int result = replyLikeMapper.delete(1L, 2L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(replyLikeMapper.existsByReplyIdAndMemberId(1L, 2L)).isFalse();
        assertThat(replyLikeMapper.existsByReplyIdAndMemberId(1L, 3L)).isTrue();
    }
}
