package com.taco.backend_demo.common.message;

import java.text.MessageFormat;

/**
 * 1. 消息解析器：根据消息码和参数解析出格式化的消息文本
 * 2. 国际化支持：从MessageTexts中获取对应语言的消息模板
 * 3. 容错处理：消息码不存在时返回原始码，格式化失败时返回原始模板
 */
public final class MessageResolver {
    
    // 1. 私有构造函数：防止外部实例化此类
    private MessageResolver() {}
    
    /**
     * 1. 解析带参数的消息：根据消息码和参数生成格式化的消息文本
     * @param messageCode 消息码（如E001、N001等）
     * @param args 消息参数，用于替换消息模板中的占位符
     * @return 格式化后的消息文本，如果消息码不存在则返回原始消息码
     */
    public static String resolveMessage(String messageCode, Object... args) {
        String messageText = MessageTexts.getMessageText(messageCode);
        if (messageText == null) {
            return messageCode; // 返回原始消息码作为降级处理
        }
        
        if (args.length == 0) {
            return messageText;
        }
        
        // 1. 使用MessageFormat进行消息格式化
        try {
            return MessageFormat.format(messageText, args);
        } catch (Exception e) {
            // 2. 格式化失败时返回原始消息模板
            return messageText;
        }
    }
    
    /**
     * 2. 解析无参数的消息：根据消息码获取原始消息文本（不进行格式化）
     * @param messageCode 消息码（如E001、N001等）
     * @return 消息文本，如果消息码不存在则返回原始消息码
     */
    public static String resolveMessage(String messageCode) {
        String messageText = MessageTexts.getMessageText(messageCode);
        return messageText != null ? messageText : messageCode;
    }
}
