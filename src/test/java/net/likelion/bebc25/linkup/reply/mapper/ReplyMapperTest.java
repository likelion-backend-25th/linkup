package net.likelion.bebc25.linkup.reply.mapper;

import net.likelion.bebc25.linkup.reply.domain.Reply;
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

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createReplyTest() {
        // given
        Reply reply = Reply.builder()
                .postId(1L)
                .memberId(2L)
                .content("테스트 댓글")
                .build();

        // when
        int result = replyMapper.insert(reply);
        Reply resultReply = replyMapper.getById(reply.getId());

        // then
        assertThat(result).isEqualTo(1);
        assertThat(reply.getId()).isNotNull();
        assertThat(resultReply.getPostId()).isEqualTo(reply.getPostId());
        assertThat(resultReply.getMemberId()).isEqualTo(reply.getMemberId());
        assertThat(resultReply.getContent()).isEqualTo(reply.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void deleteReplyTest() {
        // given
        Reply reply = Reply.builder()
                .postId(1L)
                .memberId(2L)
                .content("테스트 댓글")
                .build();
        replyMapper.insert(reply);

        // when
        int deleteResult = replyMapper.deleteById(reply.getId());

        // then
        assertThat(deleteResult).isEqualTo(1);
        assertThat(replyMapper.getById(reply.getId())).isNull();
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void updateReplyTest() {
        // given
        Reply reply = Reply.builder()
                .postId(1L)
                .memberId(2L)
                .content("테스트 댓글")
                .build();
        replyMapper.insert(reply);

        // when
        int updateResult = replyMapper.updateReplyById(reply.getId(), "수정된 내용");
        Reply resultReply = replyMapper.getById(reply.getId());
        // then
        assertThat(updateResult).isEqualTo(1);
        assertThat(resultReply.getPostId()).isEqualTo(reply.getPostId());
        assertThat(resultReply.getMemberId()).isEqualTo(reply.getMemberId());
        assertThat(resultReply.getContent()).isEqualTo("수정된 내용");
    }

    private void createReply(Long postId, Long memberId, String content) {
        jdbcTemplate.update(
                "INSERT INTO reply (post_id, member_id, content) VALUES (?, ?, ?)",
                postId, memberId, content
        );
    }


}
