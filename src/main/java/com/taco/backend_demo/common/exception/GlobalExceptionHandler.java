package com.taco.backend_demo.common.exception;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E003;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E010;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E999;

/**
 * 全局异常处理器
 * 核心逻辑：
 * 1. 自定义业务异常（BaseException）→ 友好提示 + 业务错误码
 * 2. 验证异常（MethodArgumentNotValidException, ConstraintViolationException）→ 字段级验证错误
 * 3. 所有其他异常 → 统一兜底处理（隐藏敏感信息）
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("参数验证失败：{}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // 使用字段名作为key，错误消息代码作为value
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseFactory.fail(errors, E010);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error("约束验证失败：{}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            errors.put(fieldName, violation.getMessage());
        }
        return ResponseFactory.fail(errors, E010);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Response<Object>> handleBaseException(BaseException e) {
        log.error("业务异常：{}", e.getMessageCode(), e);

        return ResponseFactory.fail(e.getMessageCode(), e.getMessageArgs());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Response<Object>> handleInternalAuthException(InternalAuthenticationServiceException e) {
        log.error("认证异常：{}", e.getMessage());

        if (e.getCause() instanceof BaseException) {
            return handleBaseException((BaseException) e.getCause());
        }

        return ResponseFactory.fail(E003);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception e) {
        log.error("系统异常：", e);

        return  ResponseFactory.fail(E999);
    }
}