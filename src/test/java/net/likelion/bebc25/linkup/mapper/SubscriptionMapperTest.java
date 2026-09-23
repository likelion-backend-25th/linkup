package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.subscription.dto.SubscriptionResponse;
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
public class SubscriptionMapperTest {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

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
}
