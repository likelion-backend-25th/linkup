package net.likelion.bebc25.linkup.report.mapper;

import net.likelion.bebc25.linkup.report.dto.ReportCreateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

@Mapper
public interface ReportMapper {
    void saveReport(
            @Param("memberId") Long memberId,
            @Param("postId") Long postId,
            @Param("request")ReportCreateRequest request
            );

//    void saveReplyReport(
//            @Param("memberId") Long memberId,
//            @Param("postId") Long postId,
//            @Param("request")ReportCreateRequest request
//    );

}
