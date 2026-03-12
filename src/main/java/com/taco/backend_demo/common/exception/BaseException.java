package com.taco.backend_demo.common.exception;

/**
 * 1. 基础异常类：所有自定义异常的父类，继承RuntimeException
 * 2. 消息码支持：存储错误消息码和参数，用于国际化消息处理
 * 3. 统一异常结构：为全局异常处理器提供标准化的异常信息格式
 */
public class BaseException extends RuntimeException {

    private final String messageCode;
    private String[] messageArgs;

    /**
     * 1. 构造函数：使用消息码创建基础异常
     * @param messageCode 错误消息码，用于国际化消息查找
     */
    public BaseException(String messageCode) {
        super();
        this.messageCode = messageCode;
    }

    /**
     * 2. 构造函数：使用消息码和参数创建基础异常
     * @param messageCode 错误消息码，用于国际化消息查找
     * @param args 消息参数，用于动态替换消息模板中的占位符
     */
    public BaseException(String messageCode, String... args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.messageArgs = args;
    }

    /**
     * 3. 构造函数：使用异常原因、消息码和参数创建基础异常
     * @param cause 异常的根本原因
     * @param messageCode 错误消息码，用于国际化消息查找
     * @param args 消息参数，用于动态替换消息模板中的占位符
     */
    public BaseException(Throwable cause, String messageCode, String... args) {
        super(messageCode, cause);
        this.messageCode = messageCode;
        this.messageArgs = args;
    }

    /**
     * 4. 获取消息码：返回错误消息的唯一标识码
     * @return 错误消息码
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * 5. 获取消息参数：返回消息模板中的动态参数
     * @return 消息参数数组
     */
    public String[] getMessageArgs() {
        return messageArgs;
    }
}
