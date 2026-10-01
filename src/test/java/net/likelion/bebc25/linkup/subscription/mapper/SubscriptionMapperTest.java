package net.likelion.bebc25.linkup.subscription.mapper;

import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscriptionDetailResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscriptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SubscriptionMapperTest {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        Long creatorId = 5L;
        Long memberId = 10L;
        String customerKey = "test_customer_key";
        String billingKey = "test_billing_key";
        int price = 4900;

        Subscription sub = Subscription.builder()
                .creatorId(creatorId)
                .memberId(memberId)
                .customerKey(customerKey)
                .billingKey(billingKey)
                .price(price)
                .build();

        int resultValue = subscriptionMapper.save(sub);
        assertThat(resultValue).isEqualTo(1);

        Subscription result = subscriptionMapper.findBySubscriptionId(sub.getSubscriptionId());
        assertThat(result).isNotNull();
        assertThat(result.getCreatorId()).isEqualTo(creatorId);
        assertThat(result.getMemberId()).isEqualTo(memberId);
        assertThat(result.getCustomerKey()).isEqualTo(customerKey);
        assertThat(result.getBillingKey()).isEqualTo(billingKey);
        assertThat(result.getPrice()).isEqualTo(price);
        System.out.println(result);
    }

    @Test
    @DisplayName("updateCancel 테스트")
    void updateCancelTest() {
        Long subId = 1L;

        int resultValue = subscriptionMapper.updateCancel(subId);
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
//    @DisplayName("deleteExpirations 테스트")
//    void deleteExpirationsTest() {
//        int resultValue = subscriptionMapper.deleteExpirations();
//        assertThat(resultValue).isEqualTo(10);
//
//        Subscription result = subscriptionMapper.findBySubscriptionId(25L);
//        assertThat(result).isNull();
//    }

    @Test
    @DisplayName("findByMemberIdAndCreatorId 테스트")
    void findByMemberIdAndCreatorIdTest() {
        Long creatorId = 5L;
        Long memberId = 2L;

        String status = subscriptionMapper.findByMemberIdAndCreatorId(memberId, creatorId);

        assertThat(status).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("특정 id 구독 내역 조회 테스트")
    void findBySubscriptionIdTest() {
        Long targetId = 1L;

        Subscription result = subscriptionMapper.findBySubscriptionId(targetId);

        assertThat(result).isNotNull();
        assertThat(result.getSubscriptionId()).isEqualTo(targetId);
        System.out.println(result);
    }

    @Test
    @DisplayName("특정 구독 상세 내역 조회 테스트")
    void findSubscriptionDetailTest() {
        Long targetId = 1L;

        SubscriptionDetailResponse result = subscriptionMapper.findSubscriptionDetail(targetId);

        assertThat(result).isNotNull();
        assertThat(result.subscriptionId()).isEqualTo(targetId);
        System.out.println(result);
    }

    @Test
    @DisplayName("findSubscribeCreatorListTest 테스트")
    void findSubscribeCreatorListTest() {
        Long targetMemberId = 1L;

        List<SubscribeCreatorListResponse> subList
                = subscriptionMapper.findSubscribeCreatorList(targetMemberId, null, 3);

        assertThat(subList).isNotNull();
        for (SubscribeCreatorListResponse sub : subList) {
            assertThat(sub.memberId()).isEqualTo(targetMemberId);
            System.out.println(sub.toString());
        }
    }

    @Test
    @DisplayName("countSubscribeCreatorTest 테스트")
    void countSubscribeCreatorTest() {
        Long targetId = 1L;

        int result = subscriptionMapper.countSubscribeCreator(targetId);

        assertThat(result).isEqualTo(28);
    }
}
