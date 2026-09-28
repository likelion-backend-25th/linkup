package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostReadAccessService {
    private final PostMapper postMapper;

    // 게시글 존재 유무 확인
    public Post getPostOrThrow(Long postId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
            );
        }
        return post;
    }

    // 구독자 여부 확인
    public void isSubscriber(Long memberId, Long authorId) {
        if (memberId.equals(authorId)) {
            return;
        }
        if (!postMapper.existsValidSubscription(memberId, authorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "구독하지 않은 게시글입니다.");
        }
    }
}
