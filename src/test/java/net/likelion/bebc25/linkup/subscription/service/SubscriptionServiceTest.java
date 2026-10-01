package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.*;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
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

    @Autowired
    private SubscriptionMapper subscriptionMapper;

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

        assertThat(response.subCreatorCount()).isEqualTo(28);
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

    // issueBillingKey 메소드를 거쳐야 해서 패스
//    @Test
//    @DisplayName("구독 등록 테스트")
//    void createSubsciptionTest() {
//        Long creatorId = 5L;
//        Long memberId = 10L;
//        String customerKey = "test_customer_key";
//        String billingKey = "test_billing_key";
//        int price = 4900;
//
//        CreateSubscriptionResponse response = subscriptionService.createSubscription(
//                memberId, new CreateSubscriptionRequest(creatorId, customerKey, price)
//        );
//        assertThat(response).isNotNull();
//
//        Subscription result = subscriptionMapper.findBySubscriptionId(response.subscriptionId());
//        assertThat(result.getCreatorId()).isEqualTo(creatorId);
//        assertThat(result.getMemberId()).isEqualTo(memberId);
//        assertThat(result.getCustomerKey()).isEqualTo(customerKey);
//        assertThat(result.getBillingKey()).isEqualTo(billingKey);
//        assertThat(result.getPrice()).isEqualTo(price);
//        System.out.println(result);
//    }

    @Test
    @DisplayName("구독 해지 테스트")
    void cancelSubscriptionTest() {
        Long subId = 1L;

        int resultValue = subscriptionService.cancelSubscription(subId);
        assertThat(resultValue).isEqualTo(1);

        Subscription result = subscriptionMapper.findBySubscriptionId(subId);
        assertThat(result.getStatus()).isEqualTo("CANCELED");
        assertThat(result.getNextBillingAt()).isNull();
        System.out.println(result);
    }

    // 이 테스트는 스케줄러랑 충돌이 나므로 왠만하면 하지 않는 걸 추천
    // 하고 싶다면 SubscriptionService에 deleteExpiredSubscriptions 메소드의
    // 스케줄러 어노테이션을 주석처리 할 것
//    @Test
//    @DisplayName("만료된 구독들 삭제 테스트")
//    void deleteExpiredSubscriptionsTest() {
//        int resultValue = subscriptionService.deleteExpiredSubscriptions();
//        assertThat(resultValue).isEqualTo(10);
//    }
}
