package com.company.asteroid.dto.response;

import com.company.asteroid.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        ErrorCode code,
        String error,
        String message
) {
}
