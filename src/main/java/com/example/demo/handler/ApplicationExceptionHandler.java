package com.example.demo.handler;

import com.example.demo.exceptions.BusinessException;
import com.example.demo.exceptions.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException(final BusinessException exception) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(exception.getErrorCode().getCode())
                .message(exception.getMessage())
                .build();
        log.info("Business exception: {}", exception.getMessage());
        log.debug(exception.getMessage(), exception);
        return ResponseEntity.status(
                exception.getErrorCode().getStatus() != null ?
                        exception.getErrorCode().getStatus() :
                        HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleException(final DisabledException exception) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.ERROR_USER_DISABLED.getCode())
                .message(ErrorCode.ERROR_USER_DISABLED.getDefaultMessage())
                .build();
        return ResponseEntity.status(ErrorCode.ERROR_USER_DISABLED.getStatus())
                .body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                .code(ErrorCode.BAD_CREDENTIALS.getCode())
                .build();
        return ResponseEntity.status(ErrorCode.BAD_CREDENTIALS.getStatus())
                .body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .code("TBD")
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exception) {
        final List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String errorCode = error.getDefaultMessage();
                    errors.add(ErrorResponse.ValidationError.builder()
                            .field(fieldName)
                            .code(errorCode)
                            .message(errorCode)
                            .build());
                });
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .validationErrors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_EXCEPTION.getCode())
                .message(ErrorCode.INTERNAL_EXCEPTION.getDefaultMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTERNAL_EXCEPTION.getStatus())
                .body(response);
    }

}
