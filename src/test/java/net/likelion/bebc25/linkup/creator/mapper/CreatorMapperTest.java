package net.likelion.bebc25.linkup.creator.mapper;

import net.likelion.bebc25.linkup.creator.dto.SubscriberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CreatorMapperTest {
    @Autowired
    private CreatorMapper creatorMapper;

    @Test
    @DisplayName("findSubscribeCreatorListTest 테스트")
    void findSubscribeCreatorListTest() {
        Long creatorId = 5L;

        List<SubscriberResponse> memberList
                = creatorMapper.findSubscriberList(creatorId, null, 3);

        assertThat(memberList).isNotNull();
        for (SubscriberResponse member : memberList) {
            assertThat(member.creatorId()).isEqualTo(creatorId);
            System.out.println(member);
        }
    }

    @Test
    @DisplayName("countSubscribeCreatorTest 테스트")
    void countSubscribeCreatorTest() {
        Long creatorId = 5L;

        int result = creatorMapper.countSubscriber(creatorId);

        assertThat(result).isEqualTo(4);
    }
}
