package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.follow.dto.FollowResponse;
import net.likelion.bebc25.linkup.follow.mapper.FollowMapper;
import net.likelion.bebc25.linkup.follow.service.FollowServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @Mock
    private FollowMapper followMapper;

    @InjectMocks
    private FollowServiceImpl followService;

    @Test
    @DisplayName("팔로우 성공")
    void addFollowTest() {

        Long memberId = 2L;
        Long targetId = 10L;

        followService.addFollow(memberId, targetId);

        verify(followMapper).addFollow(memberId, targetId);
    }

    @Test
    @DisplayName("자기 자신을 팔로우할 수 없다")
    void cannotFollowSelf() {

        Long memberId = 2L;
        Long targetId = 2L;

        assertThatThrownBy(() ->
                followService.addFollow(memberId, targetId)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신을 팔로우할 수 없습니다.");

        // 자기 자신 팔로우가 막혔으므로 Mapper가 호출되면 안 됨
        verify(followMapper, never())
                .addFollow(memberId, targetId);
    }

    @Test
    @DisplayName("정상적으로 언팔로우한다")
    void deleteFollowSuccess() {

        Long memberId = 2L;
        Long targetId = 10L;

        when(followMapper.deleteFollow(memberId, targetId))
                .thenReturn(1);

        followService.deleteFollow(memberId, targetId);

        verify(followMapper)
                .deleteFollow(memberId, targetId);
    }

    @Test
    @DisplayName("존재하지 않는 팔로우를 취소하면 예외가 발생한다")
    void deleteFollowNotFound() {

        Long memberId = 2L;
        Long targetId = 10L;

        // Mapper가 삭제한 행이 0개라고 가정
        when(followMapper.deleteFollow(memberId, targetId))
                .thenReturn(0);

        assertThatThrownBy(() ->
                followService.deleteFollow(memberId, targetId)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("팔로우 관계를 찾을 수 없습니다.");

        verify(followMapper)
                .deleteFollow(memberId, targetId);
    }

    @Test
    @DisplayName("팔로우 상태와 팔로워 수를 조회한다")
    void getFollowStatusTest() {

        Long memberId = 2L;
        Long targetId = 10L;

        when(followMapper.isFollowing(memberId, targetId))
                .thenReturn(true);

        when(followMapper.countFollowers(targetId))
                .thenReturn(251);

        FollowResponse response =
                followService.getFollowStatus(memberId, targetId);

        assertThat(response.targetId()).isEqualTo(targetId);
        assertThat(response.isFollowing()).isTrue();
        assertThat(response.followerCount()).isEqualTo(251);
    }

    @Test
    @DisplayName("이미 팔로우한 사용자를 다시 팔로우할 수 없다")
    void cannotFollowDuplicate() {

        Long memberId = 2L;
        Long targetId = 10L;

        when(followMapper.isFollowing(memberId, targetId))
                .thenReturn(true);

        assertThatThrownBy(() ->
                followService.addFollow(memberId, targetId)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 팔로우한 사용자입니다.");

        // 이미 팔로우 중이므로 INSERT가 실행되면 안 됨
        verify(followMapper, never())
                .addFollow(memberId, targetId);
    }

    @Test
    @DisplayName("팔로우하지 않은 사용자는 팔로우 상태가 false이다")
    void getFollowStatusNotFollowingTest() {

        Long memberId = 2L;
        Long targetId = 10L;

        when(followMapper.isFollowing(memberId, targetId))
                .thenReturn(false);

        when(followMapper.countFollowers(targetId))
                .thenReturn(251);

        FollowResponse response =
                followService.getFollowStatus(memberId, targetId);

        assertThat(response.targetId()).isEqualTo(targetId);
        assertThat(response.isFollowing()).isFalse();
        assertThat(response.followerCount()).isEqualTo(251);
    }
}