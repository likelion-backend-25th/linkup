package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    // 게시글 등록
    int insert(Post post);

}
