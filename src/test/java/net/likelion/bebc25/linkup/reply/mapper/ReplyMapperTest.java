package net.likelion.bebc25.linkup.reply.mapper;

import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReplyMapperTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReplyMapper replyMapper;

    @Test
    @DisplayName("댓글 조회 테스트")
    public void getAllReplyTest() {
        createReply(1L, 2L, "테스트 댓글");


        List<ReplyResponse> replyResponses = replyMapper.getByPostId(1L, null, 3);

        assertThat(replyResponses.getLast().memberId()).isEqualTo(2L);
    }

    private void createReply(Long postId, Long memberId, String content) {
        jdbcTemplate.update(
                "INSERT INTO reply (post_id, member_id, content) VALUES (?, ?, ?)",
                postId, memberId, content
        );
    }
}
