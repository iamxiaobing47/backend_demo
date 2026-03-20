package com.taco.backend_demo.common.response;

import com.taco.backend_demo.common.message.MessageResolver;
import org.springframework.http.ResponseEntity;

/**
 * 响应工厂类：提供统一的 API 响应构建方法
 *
 * 【为什么需要响应工厂】
 * 在前后端分离架构中，统一的响应格式非常重要：
 * - 前端可以根据统一格式解析响应
 * - 支持国际化，通过 messageCode 动态生成消息
 * - 便于错误处理和日志记录
 *
 * 【核心功能】
 * 1. 提供成功/失败响应的静态工厂方法
 * 2. 支持泛型数据，适应各种业务场景
 * 3. 遵循 API 规范：不直接设置 message 字段，由前端根据 messageCode 解析
 *
 * 【响应格式示例】
 * 成功响应：{ "success": true, "data": {...}, "messageCode": "SUCCESS" }
 * 失败响应：{ "success": false, "messageCode": "E001", "messageArgs": ["arg1"] }
 *
 * 【使用方法】
 * ResponseFactory.success(data)           // 带数据的成功响应
 * ResponseFactory.fail(E001)             // 失败响应
 * ResponseFactory.fail(E001, "参数")      // 带参数的失败响应
 *
 * @author taco
 */
public class ResponseFactory {

    // ==================== 成功响应 ====================

    /**
     * 【方法 1】创建空的成功响应
     * <p>
     * 适用于不需要返回数据的操作，如删除、登出等
     * <p>
     * 响应格式：
     * {
     *   "success": true,
     *   "data": null,
     *   "messageCode": null,
     *   "messageArgs": null
     * }
     *
     * @param <T> 响应数据类型（这里是 Void）
     * @return ResponseEntity<Response<T>> 成功响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> success() {
        Response<T> response = new Response<>();
        response.setSuccess(true);    // 设置成功标志
        return ResponseEntity.ok(response);  // 返回 HTTP 200
    }

    /**
     * 【方法 2】创建带数据的成功响应
     * <p>
     * 适用于需要返回业务数据的操作，如查询用户、登录等
     * <p>
     * 响应格式：
     * {
     *   "success": true,
     *   "data": {...},
     *   "messageCode": null,
     *   "messageArgs": null
     * }
     *
     * @param <T> 响应数据类型（泛型，如 UserInfo、List<User> 等）
     * @param data 业务数据
     * @return ResponseEntity<Response<T>> 成功响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> success(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);    // 设置成功标志
        response.setData(data);       // 设置业务数据
        return ResponseEntity.ok(response);  // 返回 HTTP 200
    }

    /**
     * 【方法 3】创建带消息码的成功响应
     * <p>
     * 适用于需要明确告知操作结果的场景
     * <p>
     * messageCode 作用：
     * - 前端根据 code 查找对应的国际化消息
     * - 支持多语言切换
     * - 避免硬编码消息文本
     * <p>
     * 响应格式：
     * {
     *   "success": true,
     *   "data": {...},
     *   "messageCode": "OPERATION_SUCCESS",
     *   "messageArgs": null
     * }
     *
     * @param <T> 响应数据类型
     * @param data 业务数据
     * @param messageCode 消息码，用于前端国际化处理
     *                     例如："USER_CREATED"、"LOGIN_SUCCESS"
     * @return ResponseEntity<Response<T>> 成功响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> success(T data, String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(true);          // 设置成功标志
        response.setData(data);             // 设置业务数据
        response.setMessageCode(messageCode); // 设置消息码
        response.setMessageArgs(null);      // 无消息参数
        // 不设置 message 字段，符合 API 规范
        return ResponseEntity.ok(response);
    }

    /**
     * 【方法 4】创建带消息码和参数的成功响应
     * <p>
     * 适用于需要动态生成消息的场景
     * <p>
     * messageArgs 作用：
     * - 替换消息模板中的占位符
     * - 例如：消息模板 "欢迎 {0}，您有 {1} 条新消息"
     *         参数 ["张三", "5"]
     *         结果："欢迎 张三，您有 5 条新消息"
     * <p>
     * 响应格式：
     * {
     *   "success": true,
     *   "data": {...},
     *   "messageCode": "OPERATION_SUCCESS",
     *   "messageArgs": ["arg1", "arg2"]
     * }
     *
     * @param <T> 响应数据类型
     * @param data 业务数据
     * @param messageCode 消息码，用于前端国际化处理
     * @param messageArgs 消息参数，用于动态替换消息模板中的占位符
     *                     可变参数，可以传 0 个或多个
     * @return ResponseEntity<Response<T>> 成功响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> success(T data, String messageCode, String... messageArgs) {
        Response<T> response = new Response<>();
        response.setSuccess(true);          // 设置成功标志
        response.setData(data);             // 设置业务数据
        response.setMessageCode(messageCode); // 设置消息码
        // 如果有参数则设置，否则设为 null
        response.setMessageArgs(messageArgs.length > 0 ? messageArgs : null);
        // 不设置 message 字段，符合 API 规范
        return ResponseEntity.ok(response);
    }

    // ==================== 失败响应 ====================

    /**
     * 【方法 5】创建失败响应
     * <p>
     * 适用于操作失败的场景，如参数错误、权限不足等
     * <p>
     * 响应格式：
     * {
     *   "success": false,
     *   "data": null,
     *   "messageCode": "E001",
     *   "messageArgs": null
     * }
     *
     * @param <T> 响应数据类型（这里是 Void）
     * @param messageCode 错误消息码，用于前端国际化处理
     *                     例如："E001"（用户不存在）、"E002"（账户锁定）
     * @return ResponseEntity<Response<T>> 失败响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> fail(String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);         // 设置失败标志
        response.setMessageCode(messageCode); // 设置错误消息码
        response.setMessageArgs(null);      // 无消息参数
        // 不设置 message 字段，符合 API 规范
        return ResponseEntity.ok(response);
    }

    /**
     * 【方法 6】创建带参数的失败响应
     * <p>
     * 适用于需要告知具体错误信息的场景
     * <p>
     * 例如：
     * - 错误模板："密码长度必须在 {0} 到 {1} 之间"
     * - 参数：["8", "20"]
     * - 最终消息："密码长度必须在 8 到 20 之间"
     * <p>
     * 响应格式：
     * {
     *   "success": false,
     *   "data": null,
     *   "messageCode": "VALIDATION_ERROR",
     *   "messageArgs": ["8", "20"]
     * }
     *
     * @param <T> 响应数据类型
     * @param messageCode 错误消息码，用于前端国际化处理
     * @param messageArgs 消息参数，用于动态替换消息模板中的占位符
     *                     可变参数，可以传 0 个或多个
     * @return ResponseEntity<Response<T>> 失败响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> fail(String messageCode, String... messageArgs) {
        Response<T> response = new Response<>();
        response.setSuccess(false);         // 设置失败标志
        response.setMessageCode(messageCode); // 设置错误消息码
        // 如果有参数则设置，否则设为 null
        response.setMessageArgs(messageArgs != null && messageArgs.length > 0 ? messageArgs : null);
        // 不设置 message 字段，符合 API 规范
        return ResponseEntity.ok(response);
    }

    /**
     * 【方法 7】创建带数据的失败响应
     * <p>
     * 适用于部分成功场景，如批量操作中部分失败
     * <p>
     * 响应格式：
     * {
     *   "success": false,
     *   "data": {...},  // 部分成功的数据
     *   "messageCode": "PARTIAL_SUCCESS",
     *   "messageArgs": null
     * }
     *
     * @param <T> 响应数据类型
     * @param data 业务数据（通常用于部分成功场景）
     * @param messageCode 错误消息码，用于前端国际化处理
     * @return ResponseEntity<Response<T>> 失败响应实体（HTTP 200）
     */
    public static <T> ResponseEntity<Response<T>> fail(T data, String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);         // 设置失败标志
        response.setData(data);             // 设置业务数据
        response.setMessageCode(messageCode); // 设置错误消息码
        response.setMessageArgs(null);      // 无消息参数
        // 不设置 message 字段，符合 API 规范
        return ResponseEntity.ok(response);
    }

}
