package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminReportDetailResponse(
        Long id,
        String targetType,
        String reporterName,
        String targetUserName,
        String content,
        String reason,
        String reportContent,
        LocalDateTime createdAt,
        String status
) {
}
