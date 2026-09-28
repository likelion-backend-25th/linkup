package net.likelion.bebc25.linkup.search.mapper;

import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.UserSearchFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {

    List<MemberSearchResponse> searchMembers(
            @Param("keyword") String keyword,
            @Param("filter") UserSearchFilter filter,
            @Param("memberId") Long memberId,
            @Param("cursorId") Long cursorId,
            @Param("size") int size
    );
}