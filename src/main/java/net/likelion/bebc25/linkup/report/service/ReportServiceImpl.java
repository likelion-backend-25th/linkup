package net.likelion.bebc25.linkup.report.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.PostDetailResponse;
import net.likelion.bebc25.linkup.post.service.PostService;
import net.likelion.bebc25.linkup.reply.domain.Reply;
import net.likelion.bebc25.linkup.reply.mapper.ReplyMapper;
import net.likelion.bebc25.linkup.report.dto.ReportCreateRequest;
import net.likelion.bebc25.linkup.report.mapper.ReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final PostService postService;
    private final ReplyMapper replyMapper;

    @Transactional
    @Override
    public void createdPostReport(Long memberId, Long postId, ReportCreateRequest request) {
        // 신고 대상 게시글 조회
        PostDetailResponse post = postService.getPostDetailById(postId);

        // 본인의 게시글은 신고 X
        if(memberId.equals(post.memberId())){
            throw new IllegalArgumentException("본인의 게시글은 신고할 수 없습니다.");
        }
        // 신고 등록
        reportMapper.saveReport(memberId, postId, request);
    }

    @Transactional
    @Override
    public void createdReplyReport(Long memberId, Long postId, Long replyId, ReportCreateRequest request) {

        Reply reply = replyMapper.getById(replyId);

        if(reply == null || !reply.getPostId().equals(postId)){
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }

        if(memberId.equals(reply.getMemberId())){
            throw new IllegalArgumentException("본인의 댓글은 신고할 수 없습니다.");
        }

        reportMapper.saveReplyReport(memberId, replyId, postId, reply.getMemberId(), request);
    }

}
