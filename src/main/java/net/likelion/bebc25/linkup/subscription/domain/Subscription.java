package net.likelion.bebc25.linkup.subscription.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class Subscription {
    private Long subscriptionId;
    private Long creatorId;
    private Long memberId;
    private String customerUid;
    private int price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Builder.Default
    private String status = "ACTIVE";
    private LocalDateTime nextBillingAt;
}
