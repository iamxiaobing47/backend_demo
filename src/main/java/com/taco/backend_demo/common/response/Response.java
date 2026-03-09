package com.taco.backend_demo.common.response;

/**
 * 响应对象
 */
public class Response<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息代码
     */
    private String messageCode;

    /**
     * 消息参数数组
     */
    private String[] messageArgs;

    /**
     * 完整的消息文本（可选）
     */
    private String message;
    
    public Response() {
    }
    
    public Response(boolean success, T data, String messageCode, String[] messageArgs, String message) {
        this.success = success;
        this.data = data;
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}