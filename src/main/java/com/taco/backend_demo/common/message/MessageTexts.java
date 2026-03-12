package com.taco.backend_demo.common.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. 消息文本映射类：存储所有消息码对应的实际文本内容
 * 2. 国际化基础：提供英文消息文本，支持多语言扩展
 * 3. 不可变集合：使用Collections.unmodifiableMap确保消息映射的安全性
 */
public final class MessageTexts {
    
    // 1. 私有构造函数：防止外部实例化此类
    private MessageTexts() {}
    
    // 2. 消息文本映射：存储所有消息码到文本的映射关系
    private static final Map<String, String> MESSAGE_TEXTS;
    
    static {
        Map<String, String> messages = new HashMap<>();
        
        // ==================== 1. 认证与授权错误 (E001-E009) ====================
        messages.put("E001", "Username or password incorrect");
        messages.put("E002", "User account locked");
        messages.put("E003", "Authentication failed");
        messages.put("E004", "Password verification failed");
        messages.put("E005", "Password expired");
        messages.put("E006", "Invalid refresh token");
        messages.put("E007", "Not authenticated, please login first");
        messages.put("E008", "Not authorized, please contact administrator");
        
        // ==================== 2. 参数验证错误 (E010-E019) ====================
        messages.put("E010", "Parameter error");
        messages.put("E011", "Required field missing");
        messages.put("E012", "Format error");
        messages.put("E013", "Data duplicate");
        messages.put("E014", "Field {0} cannot be empty");
        messages.put("E015", "Field {0} format error");
        messages.put("E016", "Field {0} length must be between {1} and {2}");
        messages.put("E017", "Field {0} must be greater than {1}");
        messages.put("E018", "Field {0} must be less than {1}");
        
        // ==================== 3. 资源操作错误 (E020-E029) ====================
        messages.put("E020", "Resource not found");
        messages.put("E021", "{0} not found");
        messages.put("E022", "{0} already exists");
        messages.put("E023", "{0} conflicts with {1}");
        
        // ==================== 4. 业务逻辑错误 (E030-E039) ====================
        messages.put("E030", "Permission denied");
        messages.put("E031", "User {0} permission denied");
        messages.put("E032", "User {0} session expired");
        messages.put("E033", "Session expired");
        messages.put("E034", "Forbidden access");
        messages.put("E035", "Bad request");
        
        // ==================== 5. 操作执行错误 (E040-E049) ====================
        messages.put("E040", "Create failed");
        messages.put("E041", "Update failed");
        messages.put("E042", "Delete failed");
        messages.put("E043", "{0} create failed");
        messages.put("E044", "{0} update failed");
        messages.put("E045", "{0} delete failed");
        
        // ==================== 6. 数据处理错误 (E050-E059) ====================
        messages.put("E050", "Data processing failed");
        messages.put("E051", "Data conflict");
        messages.put("E052", "Processing timeout");
        
        // ==================== 7. 系统级错误 (E060-E999) ====================
        messages.put("E060", "System maintenance");
        messages.put("E061", "Service unavailable");
        messages.put("E062", "Network error");
        messages.put("E999", "System exception, please contact administrator");
        
        MESSAGE_TEXTS = Collections.unmodifiableMap(messages);
    }
    
    /**
     * 1. 获取消息文本：根据消息码返回对应的文本内容
     * @param code 消息码（如E001、N001等）
     * @return 消息文本，如果消息码不存在则返回null
     */
    public static String getMessageText(String code) {
        return MESSAGE_TEXTS.get(code);
    }
    
    /**
     * 2. 获取消息文本（带默认值）：根据消息码返回文本，不存在时返回默认值
     * @param code 消息码（如E001、N001等）
     * @param defaultText 默认文本，当消息码不存在时返回
     * @return 消息文本或默认文本
     */
    public static String getMessageText(String code, String defaultText) {
        String message = MESSAGE_TEXTS.get(code);
        return message != null ? message : defaultText;
    }
}
