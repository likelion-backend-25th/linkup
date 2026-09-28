package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
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
        Long targetMemberId = 1L;

        List<SubscribeCreatorListResponse> subList = subscriptionService.getSubscribeCreatorList(targetMemberId);

        assertThat(subList).isNotNull();
        for (SubscribeCreatorListResponse sub : subList) {
            assertThat(sub.memberId()).isEqualTo(targetMemberId);
            System.out.println(sub.toString());
        }
    }
}
