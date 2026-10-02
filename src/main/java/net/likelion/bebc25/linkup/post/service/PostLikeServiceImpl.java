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
        validatePostReadAccess(postId, memberId);
        validatePostNotLiked(postId, memberId);

        postLikeMapper.insert(postId, memberId);
        postMapper.incrementLikeCount(postId);
    }

    @Transactional
    @Override
    public void unlikePost(Long postId, Long memberId) {
        validatePostReadAccess(postId, memberId);

        int deleteCount = postLikeMapper.delete(postId, memberId);
        if (deleteCount == 1) {
            postMapper.decrementLikeCount(postId);
        }
    }

    // 게시글이 존재하는지 확인하고 조회한다.
    private Post getPostOrThrow(Long postId, Long memberId) {
        Post post = postMapper.findById(postId, memberId);

        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        return post;
    }

    // 구독자 전용 게시글은 작성자 또는 유효한 구독자만 좋아요할 수 있다.
    private void validatePostReadAccess(Long postId, Long memberId) {
        Post post = getPostOrThrow(postId, memberId);

        if (!post.isSubscriberOnly()) {
            return;
        }

        Long authorId = post.getMemberId();

        if (memberId.equals(authorId)) {
            return;
        }

        if (!postMapper.existsValidSubscription(memberId, authorId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "구독하지 않은 게시글입니다."
            );
        }
    }

    // 이미 좋아요한 게시글에는 다시 좋아요할 수 없다.
    private void validatePostNotLiked(Long postId, Long memberId) {
        if (postLikeMapper.existsByPostIdAndMemberId(postId, memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 좋아요가 등록되어 있습니다."
            );
        }
    }
}
