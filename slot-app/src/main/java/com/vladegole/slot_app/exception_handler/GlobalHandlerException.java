package com.vladegole.slot_app.exception_handler;

import com.vladegole.slot_app.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {

        LOGGER.warn("Bad request {}", illegalArgumentException.getMessage());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setLocalDateTime(LocalDateTime.now());
        errorResponseDto.setMessage(illegalArgumentException.getMessage());
        errorResponseDto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        return errorResponseDto;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationException(MethodArgumentNotValidException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        errorResponseDto.setLocalDateTime(LocalDateTime.now());

        List<String> messages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        LOGGER.warn("Validation failed - {}", messages);

        errorResponseDto.setMessage(String.join(", ", messages));
        errorResponseDto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        return errorResponseDto;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleRuntimeException(RuntimeException e) {

        LOGGER.warn("Unhandled runtime exception treated as bad request - {}", e.getMessage(), e);

        ErrorResponseDto dto = new ErrorResponseDto();
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setMessage(e.getMessage());
        dto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        return dto;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleException(Exception e) {

        LOGGER.error("Unexpected server error", e);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setLocalDateTime(LocalDateTime.now());
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return errorResponseDto;
    }
}