package com.taco.backend_demo.common.exception;

public class BusinessException extends BaseException{
    public BusinessException(String messageCode) {
        super(messageCode);
    }

    public BusinessException(String messageCode, String... args) {
        super(messageCode, args);
    }

    public BusinessException(Throwable cause, String messageCode, String... args) {
        super(cause, messageCode, args);
    }
}
