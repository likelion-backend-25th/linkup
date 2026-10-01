package net.likelion.bebc25.linkup.payment.exception;

import lombok.Getter;
import net.likelion.bebc25.linkup.payment.dto.TossApiError;

@Getter
public class TossApiException extends RuntimeException {
    private final int httpStatus;
    private final TossApiError error;

    public TossApiException(int httpStatus, TossApiError error) {
        super(error.message());

        this.httpStatus = httpStatus;
        this.error = error;
    }

    public String getCode() {
        return error.code();
    }

    public String getTraceId() {
        return error.traceId();
    }
}
