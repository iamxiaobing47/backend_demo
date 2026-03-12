package com.taco.backend_demo.common.exception;

/**
 * 1. 业务异常类：用于处理应用程序中的业务逻辑错误
 * 2. 继承BaseException：提供统一的异常处理机制和消息编码支持
 * 3. 支持多种构造方式：基础消息码、带参数的消息码、带异常原因的消息码
 */
public class BusinessException extends BaseException {
    /**
     * 1. 构造函数：使用消息码创建业务异常
     * @param messageCode 错误消息码，用于国际化消息查找
     */
    public BusinessException(String messageCode) {
        super(messageCode);
    }

    /**
     * 2. 构造函数：使用消息码和参数创建业务异常
     * @param messageCode 错误消息码，用于国际化消息查找
     * @param args 消息参数，用于动态替换消息模板中的占位符
     */
    public BusinessException(String messageCode, String... args) {
        super(messageCode, args);
    }

    /**
     * 3. 构造函数：使用异常原因、消息码和参数创建业务异常
     * @param cause 异常的根本原因
     * @param messageCode 错误消息码，用于国际化消息查找
     * @param args 消息参数，用于动态替换消息模板中的占位符
     */
    public BusinessException(Throwable cause, String messageCode, String... args) {
        super(cause, messageCode, args);
    }
}
