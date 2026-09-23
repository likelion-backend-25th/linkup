package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.PostImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostImageMapper {
    // 이미지 등록
    void insert(PostImage postImage);

    // 해당 게시글 이미지 전체 조회
    List<PostImage> findAllByPostId(@Param("postId") Long postId);
}
