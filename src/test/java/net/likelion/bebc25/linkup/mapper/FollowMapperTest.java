package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.follow.mapper.FollowMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FollowMapperTest {

    @Autowired
    private FollowMapper followMapper;

    @Test
    @DisplayName("팔로우 추가 테스트")
    void addFollowTest() {

        Long memberId = 2L;
        Long targetId = 10L;

        int result = followMapper.addFollow(memberId, targetId);

        assertThat(result).isEqualTo(1);

        // 실제로 추가되었는지 확인
        assertThat(followMapper.isFollowing(memberId, targetId))
                .isTrue();
    }

    @Test
    @DisplayName("팔로우 추가 후 팔로워 수와 팔로잉 수가 1 증가한다")
    void addFollowIncreasesCounts() {

        Long memberId = 2L;
        Long targetId = 10L;

        int followerBefore =
                followMapper.countFollowers(targetId);

        int followingBefore =
                followMapper.countFollowings(memberId);

        followMapper.addFollow(memberId, targetId);

        assertThat(followMapper.countFollowers(targetId))
                .isEqualTo(followerBefore + 1);

        assertThat(followMapper.countFollowings(memberId))
                .isEqualTo(followingBefore + 1);
    }

    @Test
    @DisplayName("팔로우 취소 테스트")
    void deleteFollowTest() {

        Long memberId = 3L;
        Long targetId = 11L;

        // 먼저 팔로우 추가
        followMapper.addFollow(memberId, targetId);

        // 팔로우 상태 확인
        assertThat(followMapper.isFollowing(memberId, targetId))
                .isTrue();

        // 언팔로우
        int result =
                followMapper.deleteFollow(memberId, targetId);

        assertThat(result).isEqualTo(1);

        // 실제로 삭제되었는지 확인
        assertThat(followMapper.isFollowing(memberId, targetId))
                .isFalse();
    }

    @Test
    @DisplayName("팔로우 취소 후 팔로워 수와 팔로잉 수가 1 감소한다")
    void deleteFollowDecreasesCounts() {

        Long memberId = 3L;
        Long targetId = 11L;

        // 현재 팔로워/팔로잉 수
        int followerBefore =
                followMapper.countFollowers(targetId);

        int followingBefore =
                followMapper.countFollowings(memberId);

        // 팔로우
        followMapper.addFollow(memberId, targetId);

        // 팔로우 상태 확인
        assertThat(followMapper.isFollowing(memberId, targetId))
                .isTrue();

        // 팔로우 후 증가 확인
        assertThat(followMapper.countFollowers(targetId))
                .isEqualTo(followerBefore + 1);

        assertThat(followMapper.countFollowings(memberId))
                .isEqualTo(followingBefore + 1);

        // 언팔로우
        int result =
                followMapper.deleteFollow(memberId, targetId);

        assertThat(result).isEqualTo(1);

        // 팔로우 상태가 false인지 확인
        assertThat(followMapper.isFollowing(memberId, targetId))
                .isFalse();

        // 원래 숫자로 돌아왔는지 확인
        assertThat(followMapper.countFollowers(targetId))
                .isEqualTo(followerBefore);

        assertThat(followMapper.countFollowings(memberId))
                .isEqualTo(followingBefore);
    }
}
