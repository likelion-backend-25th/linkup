package net.likelion.bebc25.linkup.follow.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.follow.dto.FollowMemberPageResponse;
import net.likelion.bebc25.linkup.follow.dto.FollowMemberResponse;
import net.likelion.bebc25.linkup.follow.dto.FollowResponse;
import net.likelion.bebc25.linkup.follow.mapper.FollowMapper;
import net.likelion.bebc25.linkup.member.block.mapper.BlockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;
    private final BlockMapper blockMapper;

    // 팔로우
    @Override
    public void addFollow(Long memberId, Long targetId) {

        // 자기 자신 팔로우 방지
        if (memberId.equals(targetId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        // 양방향 차단 여부 확인
        boolean blockedByMe =
                blockMapper.existsBlock(memberId, targetId) > 0;

        boolean blockedByTarget =
                blockMapper.existsBlock(targetId, memberId) > 0;

        if (blockedByMe || blockedByTarget) {
            throw new IllegalArgumentException(
                    "차단 관계에서는 팔로우할 수 없습니다."
            );
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

    @Override
    @Transactional(readOnly = true)
    public FollowMemberPageResponse getFollowers(Long memberId, Long cursor, int size) {
        List<FollowMemberResponse> result =
                followMapper.findFollowers(memberId, cursor, size + 1);

        return toPageResponse(result, size);
    }

    @Override
    @Transactional(readOnly = true)
    public FollowMemberPageResponse getFollowings(Long memberId, Long cursor, int size) {
        List<FollowMemberResponse> result =
                followMapper.findFollowings(memberId, cursor, size + 1);

        return toPageResponse(result, size);
    }

    private FollowMemberPageResponse toPageResponse(List<FollowMemberResponse> result, int size) {
        boolean hasNext = result.size() > size;
        List<FollowMemberResponse> content = List.copyOf(
                result.subList(0, Math.min(result.size(), size))
        );
        Long nextCursor = hasNext ? content.getLast().getFollowId() : null;

        return new FollowMemberPageResponse(content, nextCursor, hasNext);
    }
}
