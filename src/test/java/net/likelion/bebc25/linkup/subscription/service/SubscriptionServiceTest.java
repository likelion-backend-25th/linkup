package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.subscription.dto.PagingSubListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscriptionDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SubscriptionServiceTest {
    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    @DisplayName("사용자의 구독 리스트 조회 테스트")
    void getSubscribeCreatorListTest() {
        // 다음 페이지가 있으면 요청 개수만 반환하고 다음 커서를 제공
        // when
        Long memberId = 1L;
        Long cursor = null;
        int size = 3;

        PagingSubListResponse response
                = subscriptionService.getSubscribeCreatorList(memberId, cursor, size);

        // then
        assertThat(response.subCreatorList())
                .extracting(SubscribeCreatorListResponse::subscriptionId)
                .containsExactly(77L, 76L, 75L);

        assertThat(response.hasNext()).isTrue();
        assertThat(response.nextCursor()).isEqualTo(75L);


        // 남은 게시글 수가 요청 개수와 같거나 적으면 다음 페이지는 없음
        // when
        memberId = 1L;
        cursor = 52L;
        size = 3;

        response = subscriptionService.getSubscribeCreatorList(memberId, cursor, size);

        // then
        assertThat(response.subCreatorList())
                .extracting(SubscribeCreatorListResponse::subscriptionId)
                .containsExactly(51L, 38L);

        assertThat(response.hasNext()).isFalse();
        assertThat(response.nextCursor()).isNull();
    }

    @Test
    @DisplayName("특정 구독 상세 내역 조회 테스트")
    void getSubscriptionDetailTest() {
        Long targetId = 1L;

        SubscriptionDetailResponse result = subscriptionService.getSubscriptionDetail(targetId);

        assertThat(result).isNotNull();
        assertThat(result.subscriptionId()).isEqualTo(targetId);
        System.out.println(result);
    }
}
