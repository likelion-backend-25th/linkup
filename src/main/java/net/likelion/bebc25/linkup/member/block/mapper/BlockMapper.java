package net.likelion.bebc25.linkup.member.block.mapper;

import net.likelion.bebc25.linkup.member.dto.BlockedMemberResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlockMapper {

    int existsBlock(
            @Param("memberId") Long memberId,
            @Param("blockedId") Long blockedId
    );

    void saveBlock(
            @Param("memberId") Long memberId,
            @Param("blockedId") Long blockedId
    );

    void deleteBlock(
            @Param("memberId") Long memberId,
            @Param("blockedId") Long blockedId
    );

    List<BlockedMemberResponseDto> findBlockedMembers(
            @Param("memberId") Long memberId
    );

    boolean existsBlockBetween(
            @Param("memberId") Long memberId,
            @Param("targetId") Long targetId
    );
}