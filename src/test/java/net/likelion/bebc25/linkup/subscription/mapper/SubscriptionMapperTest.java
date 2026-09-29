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
    @DisplayName("구독 등록 테스트")
    void saveTest() {
        Long creatorId = 5L;
        Long memberId = 10L;
        String customerUid = "test_uid";
        int price = 4900;

        Subscription sub = Subscription.builder()
                .creatorId(creatorId)
                .memberId(memberId)
                .customerUid(customerUid)
                .price(price)
                .build();

        int resultValue = subscriptionMapper.save(sub);
        assertThat(resultValue).isEqualTo(1);

        Subscription result = subscriptionMapper.findBySubscriptionId(sub.getSubscriptionId());
        assertThat(result).isNotNull();
        assertThat(result.getCreatorId()).isEqualTo(creatorId);
        assertThat(result.getMemberId()).isEqualTo(memberId);
        assertThat(result.getCustomerUid()).isEqualTo(customerUid);
        assertThat(result.getPrice()).isEqualTo(price);
        System.out.println(result);
    }

    @Test
    @DisplayName("구독 상품 전체 조회 테스트")
    void findAllTest() {
        List<SubscriptionResponse> subList = subscriptionMapper.findAll();

//        for (SubscriptionResponse sub : subList) {
//            System.out.println(sub.toString());
//        }

        assertThat(subList).isNotNull();
    }

    @Test
    @DisplayName("특정 크리에이터에 대한 구독 조회 테스트")
    void findByCreatorId() {
        Long targetCreatorId = 5L;

        List<SubscriptionResponse> subList = subscriptionMapper.findByCreatorId(targetCreatorId);

        assertThat(subList).isNotNull();
        for (SubscriptionResponse sub : subList) {
            assertThat(sub.creatorId()).isEqualTo(targetCreatorId);
            System.out.println(sub.toString());
        }
    }

    @Test
    @DisplayName("특정 사용자가 구독하고 있는 상품 조회 테스트")
    void findByMemberIdTest() {
        Long targetMemberId = 1L;

        List<SubscriptionResponse> subList = subscriptionMapper.findByMemberId(targetMemberId);

        assertThat(subList).isNotNull();
        for (SubscriptionResponse sub : subList) {
            assertThat(sub.memberId()).isEqualTo(targetMemberId);
            System.out.println(sub.toString());
        }
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
    @DisplayName("특정 사용자가 구독하고 있는 크리에이터 목록 테스트")
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
    @DisplayName("특정 구독 상세 내역 조회 테스트")
    void countSubscribeCreatorTest() {
        Long targetId = 1L;

        int result = subscriptionMapper.countSubscribeCreator(targetId);

        assertThat(result).isEqualTo(28);
    }
}
