package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.mapper.PostLikeMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeMapper postLikeMapper;
    private final PostMapper postMapper;

    @Transactional
    @Override
    public void likePost(Long postId, Long memberId) {
        validatePostReadAccessOrThrow(postId, memberId);
        if (postLikeMapper.existsByPostIdAndMemberId(postId, memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 좋아요가 되어있습니다.");
        }
        postLikeMapper.insert(postId, memberId);
        postMapper.incrementLikeCount(postId);
    }

    @Transactional
    @Override
    public void unlikePost(Long postId, Long memberId) {
        validatePostReadAccessOrThrow(postId, memberId);
        int deleteCount = postLikeMapper.delete(postId, memberId);
        if (deleteCount == 1) {
            postMapper.decrementLikeCount(postId);
        }
    }

    // 게시글 접근 권한 검증
    private void validatePostReadAccessOrThrow(Long postId, Long memberId) {
        Post post = postMapper.findById(postId);

        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.");
        }

        if (post.isSubscriberOnly()) {
            if (memberId.equals(post.getMemberId())) {
                return;
            }
            if (!postMapper.existsValidSubscription(memberId, post.getMemberId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "구독하지 않은 게시글입니다.");
            }
        }
    }
}
