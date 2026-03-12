package com.taco.backend_demo.common.exception;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 1. 全局异常处理器：统一处理应用程序中的所有异常类型
 * 2. 异常分类处理：针对不同异常类型提供相应的错误响应格式
 * 3. 安全性保障：系统异常不暴露敏感信息，仅返回通用错误码
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 1. 处理方法参数验证异常：Spring Boot Bean Validation验证失败
     * @param ex 方法参数验证异常对象
     * @return 包含字段级错误信息的失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("参数验证失败：{}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // 1. 构建字段错误映射：字段名作为key，错误消息作为value
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseFactory.fail(errors, E010);
    }

    /**
     * 2. 处理约束验证异常：JSR-303注解验证失败（如@NotNull, @Size等）
     * @param ex 约束验证异常对象
     * @return 包含属性路径和错误消息的失败响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error("约束验证失败：{}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            // 2. 提取属性路径和错误消息：构建详细的验证错误信息
            String fieldName = violation.getPropertyPath().toString();
            errors.put(fieldName, violation.getMessage());
        }
        return ResponseFactory.fail(errors, E010);
    }

    /**
     * 3. 处理业务异常：自定义的BaseException及其子类
     * @param e 业务异常对象
     * @return 包含业务错误码和参数的失败响应
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Response<Object>> handleBaseException(BaseException e) {
        log.error("业务异常：{}", e.getMessageCode(), e);
        // 3. 直接返回业务异常的错误码和参数，保持业务语义完整性
        return ResponseFactory.fail(e.getMessageCode(), e.getMessageArgs());
    }

    /**
     * 4. 处理内部认证服务异常：Spring Security认证过程中的异常
     * @param e 内部认证服务异常对象
     * @return 认证相关的失败响应
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Response<Object>> handleInternalAuthException(InternalAuthenticationServiceException e) {
        log.error("认证异常：{}", e.getMessage());
        // 4. 检查异常原因：如果是业务异常则按业务异常处理，否则返回通用认证错误
        if (e.getCause() instanceof BaseException) {
            return handleBaseException((BaseException) e.getCause());
        }
        return ResponseFactory.fail(E003);
    }

    /**
     * 5. 处理通用异常：所有未被特定处理的异常的兜底处理
     * @param e 通用异常对象
     * @return 系统级错误响应，不暴露具体异常信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception e) {
        log.error("系统异常：", e);
        // 5. 安全兜底：返回通用系统错误码，避免敏感信息泄露
        return ResponseFactory.fail(E999);
    }
}
