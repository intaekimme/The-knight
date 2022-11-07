package com.a301.theknight.global.error.handler;

import com.a301.theknight.global.error.dto.ErrorResponse;
import com.a301.theknight.global.error.errorcode.ValidationErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomRestException.class)
    public ResponseEntity<ErrorResponse> customException(CustomRestException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(BindException e){
        BindingResult bindingResult = e.getBindingResult();

        int lastIndex = bindingResult.getAllErrors().size() - 1;
        FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(lastIndex);
        ValidationErrorCode validationErrorCode = ValidationErrorCode.builder()
                .message(fieldError.getDefaultMessage())
                .parameter(fieldError.getField())
                .build();
        log.info("[Binding Error] : Field = {}, Message = {}", fieldError.getDefaultMessage(), fieldError.getField());

        return ErrorResponse.toResponseEntity(validationErrorCode);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleHttpHeaderException(HttpMediaTypeNotAcceptableException e){
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .name("Http Header : Accept Error")
                .message(e.getMessage()).build());
    }

    @MessageExceptionHandler(CustomWebSocketException.class)
    @SendTo(value = "/games/{gameId}/error")
    public ErrorResponse customWebSocketException(@DestinationVariable long gameId, CustomWebSocketException e){
        return ErrorResponse.toResponse(e.getErrorCode());
    }

}
