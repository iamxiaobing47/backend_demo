package com.taco.backend_demo.common.exception;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

import static com.taco.backend_demo.common.message.Messages.CODE_044;
import static com.taco.backend_demo.common.message.Messages.MSG_999;

/**
 * 全局异常处理器
 * 核心逻辑：
 * 1. 自定义业务异常（BaseException）→ 友好提示 + 业务错误码
 * 2. 所有其他异常 → 统一兜底处理（隐藏敏感信息）
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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

        return ResponseFactory.fail(CODE_044);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception e) {
        log.error("系统异常：", e);

        return  ResponseFactory.fail(MSG_999);
    }
}