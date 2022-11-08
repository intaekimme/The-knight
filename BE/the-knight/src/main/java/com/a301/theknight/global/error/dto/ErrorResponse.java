package com.a301.theknight.global.error.dto;

import com.a301.theknight.global.error.errorcode.ErrorCode;
import com.a301.theknight.global.error.errorcode.ValidationErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String name;
    private final String message;
    private final String status;
    private String parameter;

    public static ErrorResponse toResponse(ErrorCode errorCode){
        return ErrorResponse.builder()
                .name(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus().name())
                .build();
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .name(errorCode.name())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ValidationErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .name(errorCode.name())
                        .parameter(errorCode.getParameter())
                        .message(errorCode.getMessage())
                        .build());
    }
}
