package net.likelion.bebc25.linkup.admin.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminReportDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminReportSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminReportMapper {
    List<AdminReportResponse> findReports(AdminReportSearchRequest request);

    AdminReportDetailResponse findReportDetail(@Param("reportId") Long reportId);
}
