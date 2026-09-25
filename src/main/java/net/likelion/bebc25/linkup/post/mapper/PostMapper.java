package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    // 게시글 등록
    int insert(Post post);

    // 게시글 단 건 조회
    Post findById(@Param("postId") Long postId);

    // 게시글 삭제
    int deleteById(@Param("postId") Long postId);
}
