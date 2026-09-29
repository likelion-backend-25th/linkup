package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FeedMapperTest {

    @Autowired
    private FeedMapper feedMapper;

    @Test
    @DisplayName("내가 구독한 크리에이터 게시글 조회 테스트")
    public void findSubscriptionFeedTest() {
        // given
        Long memberId = 1L;
        Long cursor = 177L;

        // when
        // 조회 테스트
        List<PostCardResponse> result1 = feedMapper.findSubscriptionFeed(memberId, null, 10);
        // 커서 테스트
        List<PostCardResponse> result2 = feedMapper.findSubscriptionFeed(memberId, cursor, 10);

        // then
        assertThat(result1).isNotNull();
        assertThat(result1)
                .extracting(PostCardResponse::postId)
                .containsExactly(277L, 227L, 177L, 127L, 77L, 27L);
        assertThat(result1.getFirst().mainImageUrl()).isEqualTo("https://cdn.example.com/posts/277/image-1.jpg");

        assertThat(result2)
                .extracting(PostCardResponse::postId)
                .containsExactly(127L, 77L, 27L);
    }
}
