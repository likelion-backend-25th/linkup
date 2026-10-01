package net.likelion.bebc25.linkup.search.mapper;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostSearchMapper {

    // 게시글 검색
    List<PostCardResponse> searchPosts(
            @Param("keyword") String keyword,
            @Param("filter") SearchFilter filter,
            @Param("memberId") Long memberId,
            @Param("cursorId") Long cursorId,
            @Param("size") int size
    );

}