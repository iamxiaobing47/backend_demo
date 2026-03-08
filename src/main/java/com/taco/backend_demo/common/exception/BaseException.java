package com.taco.backend_demo.common.exception;

public class BaseException extends RuntimeException{

    private final String messageCode;
    private  String[] messageArgs;

    public BaseException(String messageCode) {
        super();
        this.messageCode = messageCode;
    }

    public BaseException(String messageCode,  String... args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.messageArgs = args;
    }

    public BaseException(Throwable cause,String messageCode,  String... args) {
        super(messageCode, cause);
        this.messageCode = messageCode;
        this.messageArgs = args;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String[] getMessageArgs() {
        return messageArgs;
    }
}
