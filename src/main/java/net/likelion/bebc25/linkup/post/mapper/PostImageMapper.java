package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.PostImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostImageMapper {
    // 이미지 등록
    void insert(PostImage postImage);
}
