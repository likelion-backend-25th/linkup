package net.likelion.bebc25.linkup.payment.mapper;

import net.likelion.bebc25.linkup.payment.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    int save(Payment payment);
}
