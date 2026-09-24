package net.likelion.bebc25.linkup.report.service;

import net.likelion.bebc25.linkup.report.dto.ReportCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    void createdPostReport(Long memberId, Long postId, ReportCreateRequest request);

//    void createdReplyReport(Long memerId, Long postId, ReportCreateRequest request);
}
