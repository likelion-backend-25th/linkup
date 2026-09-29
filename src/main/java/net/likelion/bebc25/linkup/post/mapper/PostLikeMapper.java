package net.likelion.bebc25.linkup.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper {
    // 게시글 좋아요 등록
    int insert(@Param("postId") Long postId, @Param("memberId") Long memberId);

    // 게시글 좋아요 취소
    int delete(@Param("postId") Long postId, @Param("memberId") Long memberId);

    // 게시글 좋아요 존재 여부 조회
    boolean existsByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);
}
