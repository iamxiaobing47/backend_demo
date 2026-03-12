package com.taco.backend_demo.common.response;

import com.taco.backend_demo.common.message.MessageResolver;
import org.springframework.http.ResponseEntity;

/**
 * 1. 响应工厂类：提供统一的API响应构建方法
 * 2. 支持成功和失败响应：包含数据、消息码、消息参数等信息
 * 3. 遵循API规范：不直接设置message字段，由前端根据messageCode解析
 */
public class ResponseFactory {

    /**
     * 1. 创建空的成功响应：仅包含success=true状态
     * @param <T> 响应数据类型
     * @return 成功响应实体
     */
    public static <T> ResponseEntity<Response<T>> success() {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. 创建带数据的成功响应：包含success=true状态和业务数据
     * @param <T> 响应数据类型
     * @param data 业务数据
     * @return 成功响应实体
     */
    public static <T> ResponseEntity<Response<T>> success(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. 创建带消息码的成功响应：包含success=true状态、业务数据和消息码
     * @param <T> 响应数据类型
     * @param data 业务数据
     * @param messageCode 消息码，用于前端国际化处理
     * @return 成功响应实体
     */
    public static <T> ResponseEntity<Response<T>> success(T data, String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessageCode(messageCode);
        response.setMessageArgs(null);
        // Do not set message field to comply with API specification
        return ResponseEntity.ok(response);
    }

    /**
     * 4. 创建带消息码和参数的成功响应：包含success=true状态、业务数据、消息码和消息参数
     * @param <T> 响应数据类型
     * @param data 业务数据
     * @param messageCode 消息码，用于前端国际化处理
     * @param messageArgs 消息参数，用于动态替换消息模板中的占位符
     * @return 成功响应实体
     */
    public static <T> ResponseEntity<Response<T>> success(T data, String messageCode, String... messageArgs) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessageCode(messageCode);
        response.setMessageArgs(messageArgs.length > 0 ? messageArgs : null);
        // Do not set message field to comply with API specification
        return ResponseEntity.ok(response);
    }

    /**
     * 5. 创建失败响应：包含success=false状态和错误消息码
     * @param <T> 响应数据类型
     * @param messageCode 错误消息码，用于前端国际化处理
     * @return 失败响应实体
     */
    public static <T> ResponseEntity<Response<T>> fail(String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessageCode(messageCode);
        response.setMessageArgs(null);
        // Do not set message field to comply with API specification
        return ResponseEntity.ok(response);
    }

    /**
     * 6. 创建带参数的失败响应：包含success=false状态、错误消息码和消息参数
     * @param <T> 响应数据类型
     * @param messageCode 错误消息码，用于前端国际化处理
     * @param messageArgs 消息参数，用于动态替换消息模板中的占位符
     * @return 失败响应实体
     */
    public static <T> ResponseEntity<Response<T>> fail(String messageCode, String... messageArgs) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessageCode(messageCode);
        response.setMessageArgs(messageArgs != null && messageArgs.length > 0 ? messageArgs : null);
        // Do not set message field to comply with API specification
        return ResponseEntity.ok(response);
    }
    
    /**
     * 7. 创建带数据的失败响应：包含success=false状态、业务数据和错误消息码
     * @param <T> 响应数据类型
     * @param data 业务数据（通常用于部分成功场景）
     * @param messageCode 错误消息码，用于前端国际化处理
     * @return 失败响应实体
     */
    public static <T> ResponseEntity<Response<T>> fail(T data, String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setData(data);
        response.setMessageCode(messageCode);
        response.setMessageArgs(null);
        // Do not set message field to comply with API specification
        return ResponseEntity.ok(response);
    }

}
