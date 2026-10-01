package net.likelion.bebc25.linkup.payment.mapper;

import net.likelion.bebc25.linkup.payment.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface PaymentMapper {
    int save(Payment payment);

    Payment findBySubscriptionId(Long subscriptionId);

    String findBillingDate(Long subscriptionId);
}
