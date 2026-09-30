package net.likelion.bebc25.linkup.creator.mapper;

import net.likelion.bebc25.linkup.creator.dto.SubscriberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CreatorMapper {
    List<SubscriberResponse> findSubscriberList(
            @Param("creatorId") Long creatorId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    int countSubscriber(@Param("creatorId") Long creatorId);
}
