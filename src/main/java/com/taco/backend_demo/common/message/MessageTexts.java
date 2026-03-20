package com.taco.backend_demo.common.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息文本映射类：存储所有消息码对应的实际文本内容
 * 国际化基础：提供英文消息文本，支持多语言扩展
 * 不可变集合：使用 Collections.unmodifiableMap 确保消息映射的安全性
 */
public final class MessageTexts {

    // 私有构造函数：防止外部实例化此类
    private MessageTexts() {}

    // 消息文本映射：存储所有消息码到文本的映射关系
    private static final Map<String, String> MESSAGE_TEXTS;

    static {
        Map<String, String> messages = new HashMap<>();

        // ==================== 成功消息 (N001-N019) ====================
        messages.put("N001", "Created successfully");
        messages.put("N002", "Updated successfully");
        messages.put("N003", "Deleted successfully");

        // ==================== 认证相关 (N020-N029) ====================
        messages.put("N021", "Logged out successfully");

        // ==================== 认证与授权错误 (E001-E009) ====================
        messages.put("E001", "Username or password incorrect");
        messages.put("E002", "User account locked");
        messages.put("E003", "Authentication failed");
        messages.put("E004", "Password verification failed");
        messages.put("E006", "Invalid refresh token");
        messages.put("E007", "Not authenticated, please login first");
        messages.put("E008", "Not authorized, please contact administrator");

        // ==================== 参数验证错误 (E010-E019) ====================
        messages.put("E010", "Parameter error");
        messages.put("E014", "Field {0} cannot be empty");
        messages.put("E015", "Field {0} format error");
        messages.put("E016", "Field {0} length must be between {1} and {2}");

        // ==================== 系统级错误 (E060-E999) ====================
        messages.put("E999", "System exception, please contact administrator");

        MESSAGE_TEXTS = Collections.unmodifiableMap(messages);
    }

    /**
     * 获取消息文本：根据消息码返回对应的文本内容
     * @param code 消息码（如 E001、N001 等）
     * @return 消息文本，如果消息码不存在则返回 null
     */
    public static String getMessageText(String code) {
        return MESSAGE_TEXTS.get(code);
    }

    /**
     * 获取消息文本（带默认值）：根据消息码返回文本，不存在时返回默认值
     * @param code 消息码（如 E001、N001 等）
     * @param defaultText 默认文本，当消息码不存在时返回
     * @return 消息文本或默认文本
     */
    public static String getMessageText(String code, String defaultText) {
        String message = MESSAGE_TEXTS.get(code);
        return message != null ? message : defaultText;
    }
}
