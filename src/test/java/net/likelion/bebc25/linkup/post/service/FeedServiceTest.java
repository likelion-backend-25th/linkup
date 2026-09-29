package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FeedServiceTest {

    @Autowired
    private FeedService feedService;

    @Test
    @DisplayName("다음 페이지가 있으면 요청 개수만 반환하고 다음 커서를 제공")
    void getSubscriptionFeedWithNextPage() {
        // when
        FeedResponse response =
                feedService.getSubscriptionFeed(1L, null, 3);

        // then
        assertThat(response.posts())
                .extracting(PostCardResponse::postId)
                .containsExactly(277L, 227L, 177L);

        assertThat(response.hasNext()).isTrue();
        assertThat(response.nextCursor()).isEqualTo(177L);
    }

    @Test
    @DisplayName("남은 게시글 수가 요청 개수와 같으면 다음 페이지는 없음")
    void getSubscriptionFeedLastPage() {
        // when
        FeedResponse response =
                feedService.getSubscriptionFeed(1L, 177L, 3);

        // then
        assertThat(response.posts())
                .extracting(PostCardResponse::postId)
                .containsExactly(127L, 77L, 27L);

        assertThat(response.hasNext()).isFalse();
        assertThat(response.nextCursor()).isNull();
    }
}
