package com.taco.backend_demo.common.response;

/**
 * 1. 统一响应对象：封装API接口的返回数据结构
 * 2. 国际化支持：通过messageCode和messageArgs实现多语言消息处理
 * 3. 灵活数据载体：泛型T支持任意类型的数据返回
 */
public class Response<T> {

    /**
     * 1. 操作成功标识：true表示操作成功，false表示操作失败
     */
    private boolean success;

    /**
     * 2. 业务数据载体：泛型T可以承载任意类型的业务数据
     */
    private T data;

    /**
     * 3. 消息码标识：用于前端国际化处理的错误/成功消息码
     */
    private String messageCode;

    /**
     * 4. 消息参数数组：用于动态替换消息模板中的占位符
     */
    private String[] messageArgs;

    /**
     * 5. 完整消息文本：可选字段，通常由前端根据messageCode解析生成
     */
    private String message;
    
    /**
     * 6. 默认构造函数：创建空的响应对象
     */
    public Response() {
    }
    
    /**
     * 7. 全参构造函数：创建包含所有字段的响应对象
     * @param success 操作成功标识
     * @param data 业务数据
     * @param messageCode 消息码
     * @param messageArgs 消息参数
     * @param message 完整消息文本
     */
    public Response(boolean success, T data, String messageCode, String[] messageArgs, String message) {
        this.success = success;
        this.data = data;
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
        this.message = message;
    }

    /**
     * 8. 获取操作成功标识
     * @return true表示成功，false表示失败
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 9. 设置操作成功标识
     * @param success 操作成功标识
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 10. 获取业务数据
     * @return 业务数据对象
     */
    public T getData() {
        return data;
    }

    /**
     * 11. 设置业务数据
     * @param data 业务数据对象
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 12. 获取消息码
     * @return 消息码字符串
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * 13. 设置消息码
     * @param messageCode 消息码字符串
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * 14. 获取消息参数数组
     * @return 消息参数数组
     */
    public String[] getMessageArgs() {
        return messageArgs;
    }

    /**
     * 15. 设置消息参数数组
     * @param messageArgs 消息参数数组
     */
    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    /**
     * 16. 获取完整消息文本
     * @return 完整消息文本
     */
    public String getMessage() {
        return message;
    }

    /**
     * 17. 设置完整消息文本
     * @param message 完整消息文本
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
