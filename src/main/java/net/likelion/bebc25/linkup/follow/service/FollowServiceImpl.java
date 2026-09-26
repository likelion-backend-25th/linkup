package net.likelion.bebc25.linkup.follow.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.follow.dto.FollowResponse;
import net.likelion.bebc25.linkup.follow.mapper.FollowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;

    // 팔로우
    @Override
    public void addFollow(Long memberId, Long targetId) {

        // 자기 자신 팔로우 방지
        if (memberId.equals(targetId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        // 중복 팔로우 방지
        if (followMapper.isFollowing(memberId, targetId)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        followMapper.addFollow(memberId, targetId);
        followMapper.incrementFollowingCount(memberId);
        followMapper.incrementFollowerCount(targetId);
    }

    // 언팔로우
    @Override
    public void deleteFollow(Long memberId, Long targetId) {

        int result = followMapper.deleteFollow(memberId, targetId);

        // 존재하지 않는 팔로우 관계인 경우
        if (result == 0) {
            throw new IllegalArgumentException("팔로우 관계를 찾을 수 없습니다.");
        }

        followMapper.decrementFollowingCount(memberId);
        followMapper.decrementFollowerCount(targetId);
    }

    // 팔로우 상태 조회
    @Override
    public boolean isFollowing(Long memberId, Long targetId) {
        return followMapper.isFollowing(memberId, targetId);
    }

    // 팔로워 수
    @Override
    public int countFollowers(Long targetId) {
        return followMapper.countFollowers(targetId);
    }

    // 팔로잉 수
    @Override
    public int countFollowings(Long memberId) {
        return followMapper.countFollowings(memberId);
    }

    // 팔로우 상태 및 팔로워 수 조회
    @Override
    public FollowResponse getFollowStatus(
            Long memberId,
            Long targetId
    ) {

        // 팔로우 상태 확인
        boolean isFollowing =
                followMapper.isFollowing(memberId, targetId);

        // 팔로워 수 확인
        int followerCount =
                followMapper.countFollowers(targetId);

        // 팔로잉 수 확인
        int followingCount =
                followMapper.countFollowings(memberId);

        return new FollowResponse(
                targetId,
                isFollowing,
                followerCount,
                memberId,
                followingCount
        );
    }
}