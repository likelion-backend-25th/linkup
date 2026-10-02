package net.likelion.bebc25.linkup.creator.service;

import net.likelion.bebc25.linkup.creator.dto.PagingSubscriberListResponse;
import net.likelion.bebc25.linkup.creator.dto.SubscriberResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CreatorServiceTest {
    @Autowired
    private CreatorService creatorService;

    @Test
    @DisplayName("크리에이터인 사용자의 구독자 리스트 조회 테스트")
    void getSubscribeCreatorListTest() {
        // 다음 페이지가 있으면 요청 개수만 반환하고 다음 커서를 제공
        // when
        Long creatorId = 5L;
        Long cursor = null;
        int size = 3;

        PagingSubscriberListResponse response
                = creatorService.getSubscriberList(creatorId, cursor, size);

        // then
        assertThat(response.subscriberList())
                .extracting(SubscriberResponse::subscriptionId)
                .containsExactly(5L, 4L, 3L);

        assertThat(response.subscriberCount()).isEqualTo(5);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.nextCursor()).isEqualTo(3L);


        // 남은 게시글 수가 요청 개수와 같거나 적으면 다음 페이지는 없음
        // when
        cursor = 2L;
        size = 3;

        response = creatorService.getSubscriberList(creatorId, cursor, size);

        // then
        assertThat(response.subscriberList())
                .extracting(SubscriberResponse::subscriptionId)
                .containsExactly(1L);

        assertThat(response.hasNext()).isFalse();
        assertThat(response.nextCursor()).isNull();
    }
}
