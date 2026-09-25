package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminReportResponse(
        Long id,
        String targetType,
        String reporterName,
        String targetUserName,
        String content,
        String reason,
        LocalDateTime createdAt,
        String status
) {
}
