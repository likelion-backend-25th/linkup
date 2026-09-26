package net.likelion.bebc25.linkup.reply.mapper;

import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {
    List<ReplyResponse> getByPostId(
            @Param("postId") Long postId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit);
}
