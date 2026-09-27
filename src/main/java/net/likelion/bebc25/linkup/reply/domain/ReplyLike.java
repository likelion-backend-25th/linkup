package net.likelion.bebc25.linkup.reply.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyLike {
    private Long id;
    private Long replyId;
    private Long memberId;
    private LocalDateTime createdAt;
}
