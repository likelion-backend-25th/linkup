package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.FollowingFeedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class FeedServiceTest {

    @Autowired
    private FeedService feedService;

    @Test
    @DisplayName("팔로잉 피드를 커서 방식 조회 테스트")
    void getFollowingFeed() {
        // when
        FollowingFeedResponse response =
                feedService.getFollowingFeed(1L, null, 10);

        // then
        assertThat(response).isNotNull();
        assertThat(response.posts().size()).isLessThanOrEqualTo(10);

        if (!response.posts().isEmpty()) {
            Long lastPostId = response.posts()
                    .get(response.posts().size() - 1)
                    .postId();

            assertThat(response.nextCursor()).isEqualTo(lastPostId);
        }
    }
}
